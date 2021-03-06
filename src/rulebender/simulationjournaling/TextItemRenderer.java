package rulebender.simulationjournaling;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
 
import prefuse.visual.VisualItem;
import prefuse.render.ShapeRenderer;
import prefuse.util.FontLib;
import rulebender.simulationjournaling.StringAbbreviator;

/**
 * Renders an item as a text string. The text string used by default is the
 * value of the "label" attribute. To use a different attribute, use the
 * <code>setTextAttributeName</code> method. To perform custom String
 * selection, simply subclass this Renderer and override the 
 * <code>getText</code> method.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class TextItemRenderer extends ShapeRenderer {
    public static final int ALIGNMENT_LEFT   = 0;
    public static final int ALIGNMENT_RIGHT  = 1;
    public static final int ALIGNMENT_CENTER = 2;
    public static final int ALIGNMENT_BOTTOM = 1;
    public static final int ALIGNMENT_TOP    = 0; 

    protected String m_labelName;
    protected int m_xAlign = ALIGNMENT_CENTER;
    protected int m_yAlign = ALIGNMENT_CENTER;
    protected int m_horizBorder = 3;
    protected int m_vertBorder = 0;

    protected int m_maxTextWidth = -1;
    protected int m_abbrevType = StringAbbreviator.TRUNCATE;
    protected StringAbbreviator m_abbrev = StringAbbreviator.getInstance();
    
    protected RectangularShape m_textBox  = new Rectangle2D.Float();
    protected Font m_font = FontLib.getFont("SansSerif", Font.PLAIN, 10);
    
    protected Point2D     m_tmpPoint = new Point2D.Float();

    /**
     * Constructor with default label name
     */
	public TextItemRenderer() {
		m_labelName = "label";
	} //TextItemRenderer (constructor)
	
	/**
	 * New constructor for use with the TreeView / TimelineView views
	 */
	public TextItemRenderer(String item) {
		m_labelName = item;
	} //TextItemRenderer (constructor)

    /**
     * Sets the default font used by this Renderer. If calling getFont() on
     * a VisualItem returns a non-null value, the returned Font will be used
     * instead of this default one.
     * @param f the default font to use. Note that this will ONLY be used when
     * if a VisualItem's getFont() method returns null.
     */
	public void setFont(Font f) {
        m_font = f;
    } //setFont
    
    /**
     * Rounds the corners of the bounding rectangle in which the text
     * string is rendered.
     * @param arcWidth the width of the curved corner
     * @param arcHeight the height of the curved corner
     */
	public void setRoundedCorner(int arcWidth, int arcHeight) {
        if ( (arcWidth == 0 || arcHeight == 0) && !(m_textBox instanceof Rectangle2D) ) {
            m_textBox = new Rectangle2D.Float();
        } else {
            if ( !(m_textBox instanceof RoundRectangle2D) ) {
                m_textBox = new RoundRectangle2D.Float();
            } //if
            
            ((RoundRectangle2D)m_textBox).setRoundRect(0,0,10,10,arcWidth,arcHeight);                    
        } //if-else
    } // setRoundedCorner

    /**
     * Get the attribute name of the text to draw.
     * @return the text tattribute name
     */
	public String getTextAttributeName() {
        return m_labelName;
    } //getTextAttributeName
    
    /**
     * Set the attribute name for the text to draw.
     * @param name the text attribute name
     */
	public void setTextAttributeName(String name) {
        m_labelName = name;
    } //setTextAttributeName

    /**
     * Sets the maximum width that should be allowed of the text label.
     * A value of -1 specifies no limit (this is the default).
     * @param maxWidth the maximum width of the text or -1 for no limit
     */
	public void setMaxTextWidth(int maxWidth) {
        m_maxTextWidth = maxWidth;
    } //setMaxTextWidth
    
    /**
     * Sets the type of abbreviation to be used if a text label is longer
     * than the maximum text width. The value should be one of the constants
	       * provided by the {@link edu.berkeley.guir.prefuse.util.StringAbbreviator
     * StringAbbreviator} class. To enable abbreviation, you must first set
	       * the maximum text width using the {@link #setMaxTextWidth(int) 
     * setMaxTextWidth} method.
     * @param abbrevType the abbreviation type to use. Should be one of the
     * constants provided by the 
	       * {@link edu.berkeley.guir.prefuse.util.StringAbbreviator
     * StringAbbreviator} class (e.g. StringAbbreviator.TRUNCATE or 
     * StringAbbreviator.NAME).
     */
	public void setAbbrevType(int abbrevType) {
        m_abbrevType = abbrevType;
    } //setAbbrevType
    
    /**
     * Returns the text to draw. Subclasses can override this class to
     * perform custom text rendering.
     * @param item the item to represent as a <code>String</code>
     * @return a <code>String</code> to draw
     */
	protected String getText(VisualItem item) {
		//TODO: make sure this is the right change from getAttribute()
        String s = (String)item.getString(m_labelName);
	        if ( m_maxTextWidth > -1 ) {
	        	Font font = item.getFont();
	        	
	        	if ( font == null ) { 
	        		font = m_font; 
	        	} //if
	        	
            	FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(font);
	            
            	if ( fm.stringWidth(s) > m_maxTextWidth ) {
            		s = m_abbrev.abbreviate(s, m_abbrevType, fm, m_maxTextWidth);            
            	} //if
	        } //if
        return s;
    } // getText
    
	/**
	 * Determine if the TextItem is a hyperlink (unused)
	 * @param item
	 * @return
	 */
	protected boolean isHyperlink(VisualItem item) {
        //TODO: uncomment and fix
		//Boolean b = (Boolean)item.getVizAttribute(m_labelName + "_LINK");
        //return ( b != null && Boolean.TRUE.equals(b) );
		return false;
    } // isHyperlink

    /**
     * @see edu.berkeley.guir.prefuse.render.ShapeRenderer#getRawShape(edu.berkeley.guir.prefuse.VisualItem)
     */
	protected Shape getRawShape(VisualItem item) {
        String s = getText(item);
        if (s == null) { 
        	s = ""; 
        } //if
        
        m_font = item.getFont();
        
        // make renderer size-aware
        double size = item.getSize();
        if (size != 1) {
            m_font = FontLib.getFont(m_font.getName(), m_font.getStyle(), size*m_font.getSize());
        } //if
        
        FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(m_font);
        double h = fm.getHeight() + size*2*m_vertBorder;
        double w = fm.stringWidth(s) + size*2*m_horizBorder;
        getAlignedPoint(m_tmpPoint, item, w, h, m_xAlign, m_yAlign);
        m_textBox.setFrame(m_tmpPoint.getX(),m_tmpPoint.getY(),w,h);
        return m_textBox;
    } //
    
    /**
     * Helper method, which calculates the top-left co-ordinate of a node
     * given the node's alignment.
     */
    protected static void getAlignedPoint(Point2D p, VisualItem item, double w, double h, int xAlign, int yAlign) {
        double x = item.getX(), y = item.getY();
	    
        if ( xAlign == ALIGNMENT_CENTER ) {
	       	x = x-(w/2);
	    } else if ( xAlign == ALIGNMENT_RIGHT ) {
	      	x = x-w;
	    } //if-else
	        
	    if ( yAlign == ALIGNMENT_CENTER ) {
	       	y = y-(h/2);
	    } else if ( yAlign == ALIGNMENT_BOTTOM ) {
	       	y = y-h;
	    } //if-else
        
	    p.setLocation(x,y);
    } // getAlignedPoint

    /**
     * @see edu.berkeley.guir.prefuse.render.Renderer#render(java.awt.Graphics2D, edu.berkeley.guir.prefuse.VisualItem)
     */
	public void render(Graphics2D g, VisualItem item) {
        Shape shape = getShape(item);
	    
        if (shape != null) {
            super.drawShape(g, item, shape);
       
            // now render the text
            String s = getText(item);
	              
            if (s != null) {            
                Rectangle2D r = shape.getBounds2D();
                //TODO: double-check that I made the right modification from getColor() and to convert to a Color object
                g.setPaint(new Color(item.getTextColor()));
                g.setFont(m_font);
                FontMetrics fm = g.getFontMetrics();
                double size = item.getSize();
                double x = r.getX() + size*m_horizBorder;
                double y = r.getY() + size*m_vertBorder;
                g.drawString(s, (float)x, (float)y+fm.getAscent());
	                  
                if ( isHyperlink(item) ) {
                    int lx = (int)Math.round(x), ly = (int)Math.round(y);
                    g.drawLine(lx,ly,lx+fm.stringWidth(s),ly+fm.getHeight()-1);
                } //if
            } //if
        } //if
    } //render
    
    /**
     * Get the horizontal alignment of this node with respect to it's
     * location co-ordinate.
     * @return the horizontal alignment
     */
	public int getHorizontalAlignment() {
        return m_xAlign;
    } //getHorizontalAlignment
    
    /**
     * Get the vertical alignment of this node with respect to it's
     * location co-ordinate.
     * @return the vertical alignment
     */
	public int getVerticalAlignment() {
        return m_yAlign;
    } //getVerticalAlignment
    
    /**
     * Set the horizontal alignment of this node with respect to it's
     * location co-ordinate.
     * @param align the horizontal alignment
     */    
	public void setHorizontalAlignment(int align) {
        m_xAlign = align;
    } //setHoritontalAlignment
    
    /**
     * Set the vertical alignment of this node with respect to it's
     * location co-ordinate.
     * @param align the vertical alignment
     */    
	public void setVerticalAlignment(int align) {
        m_yAlign = align;
    } //setVerticalAlignment
    
    /**
     * Returns the amount of padding in pixels between text 
     * and the border of this item along the horizontal dimension.
     * @return the horizontal padding
     */
	public int getHorizontalPadding() {
        return m_horizBorder;
    } //getHorizontalPadding
    
    /**
     * Sets the amount of padding in pixels between text 
     * and the border of this item along the horizontal dimension.
     * @param xpad the horizontal padding to set
     */
    public void setHorizontalPadding(int xpad) {
        m_horizBorder = xpad;
    } //setHorizontalPadding
    
    /**
     * Returns the amount of padding in pixels between text 
     * and the border of this item along the vertical dimension.
     * @return the vertical padding
     */
	public int getVerticalPadding() {
        return m_vertBorder;
    } //getVerticalPadding
    
    /**
     * Sets the amount of padding in pixels between text 
     * and the border of this item along the vertical dimension.
     * @param ypad the vertical padding
     */
	public void setVerticalPadding(int ypad) {
        m_vertBorder = ypad;
    } //setVerticalPadding
    
} // end of class TextItemRenderer
