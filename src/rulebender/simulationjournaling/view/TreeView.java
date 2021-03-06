package rulebender.simulationjournaling.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.text.Document;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.LocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.filter.FisheyeTreeFilter;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.controls.ControlAdapter;
import prefuse.controls.FocusControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Tree;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.io.TreeMLReader;
import prefuse.data.search.PrefixSearchTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.ShapeRenderer;
import rulebender.simulationjournaling.Message;
import rulebender.simulationjournaling.TextItemRenderer;
import rulebender.simulationjournaling.listeners.interfaces.SmallMultipleSelectionListener;
import rulebender.simulationjournaling.listeners.interfaces.TimelineItemSelectionListener;
import rulebender.simulationjournaling.model.TimelineItem;
import rulebender.simulationjournaling.model.TimelineLoader;
import rulebender.simulationjournaling.model.TreeCreator;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.PrefuseLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.sort.TreeDepthItemSorter;

/**
 * Demonstration of a node-link tree viewer
 *
 * @version 1.0
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */

public class TreeView extends JPanel {

	private static final long serialVersionUID = 3175624563371866408L;

	// Temp hardcoded stuff
    //private String m_dir = "C:\\Users\\John\\runtime-rulebender.product\\fceri\\fceri.info";
	//private String m_dir = "C:\\Users\\John\\runtime-rulebender.product\\stat\\stat.info";
    private String m_dir = null;
    
	//private static String COMPONENT_GRAPH = "component_graph";
    
	// Color choices
    private Color BACKGROUND = Color.WHITE;
    private Color FOREGROUND = Color.BLACK;
		
    // Size of the treeview
    Dimension m_overallSize;
    
	// The TimelineView that this panel is associated with
	TimelineView m_view;
	
	// The actual tree structure
	MyTreeView tview;
	
	// Tree setup stuff
    private static final String tree = "tree";
    private static final String treeNodes = "tree.nodes";
    private static final String treeEdges = "tree.edges";
    
    // Renderers
    private TextItemRenderer m_nodeRenderer;
    private EdgeRenderer m_edgeRenderer;
    
    private String m_label = "label";
    private int m_orientation = Constants.ORIENT_LEFT_RIGHT;
    
	// Temp global variables for calculation speed results
	//long startTime, endParserTime, endDisplayTime;
	
	// Model selected in the small multiples view
	//private String m_selectedModelFromSmallMultiplesView;
	
	// Registered SmallMultiples objects
	//private ArrayList<TimelineItemSelectionListener> observers = new ArrayList<TimelineItemSelectionListener>();
	
    /**
     * Constructor:
     *  - Loads timeline info from the .INFO file
     *  - Calls TimelineLoader to convert the info to XML
     *  - Loads tree into panel
     * 
     * @param size - Size of the panel
     * @param tv - TimelineView
     */
    public TreeView(Dimension size, TimelineView tv, String path) {

    	m_overallSize = size;
    	m_view = tv;
    	m_dir = path;
    	
    	if (path == null) {
    		//tview = new MyTreeView(new Tree(), m_label, m_overallSize);
    		return;
    	} //if
    	
    	// Temp hardcoded stuff
        Tree t = null;
        //m_label = label;
        m_label = "name";
        	
        //startTime = System.nanoTime();

        // The actual data from the .INFO file
    	TimelineLoader loader = new TimelineLoader(m_dir);
    	ArrayList<TimelineItem> files = loader.parseFile();
    	String xml = loader.createXML(files);
    	printTimelineItems(files);
    		
    	//endParserTime = System.nanoTime();
    		
    		
    		
    	// Load the data into a tree from the XML representation
        try {
           	String myPath = writeXMLToFile(xml, m_dir);
           	t = (Tree)new TreeMLReader().readGraph(myPath);
            //t = (Tree)new TreeMLReader().readGraph(m_path);
           	
         	//t = (Tree)new TreeMLReader().readGraph(new ByteArrayInputStream(xml.getBytes()));
           	//t = (Tree)new TreeMLReader().readGraph(xml);
           	//t = (Tree)new TreeCreator().readGraph(loadXMLFromString(xml));
        } catch (Exception e) {
            e.printStackTrace();
            //System.exit(1);
        } //try-catch
    		
    		
            
            
            
            
        if (!(t == null)) {
         	// create a new treemap
        	tview = null;
           	tview = new MyTreeView(t, m_label, m_overallSize);
           	tview.setBackground(BACKGROUND);
           	tview.setForeground(FOREGROUND);
        } //if

    	    	
        
        // Remove extra search panel stuff (for the moment, at least)
        /*
        // create a search panel for the tree map
        JSearchPanel searchPanel = new JSearchPanel(tview.getVisualization(), treeNodes, Visualization.SEARCH_ITEMS, m_label, true, true);
        searchPanel.setShowResultCount(true);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5,5,4,0));
        searchPanel.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 11));
        searchPanel.setBackground(BACKGROUND);
        searchPanel.setForeground(FOREGROUND);
        */
        
        /*
        final JFastLabel title = new JFastLabel("THIS IS A TITLE");
        //final JFastLabel title = new JFastLabel("                 ");
        title.setPreferredSize(new Dimension(350, 20));
        title.setVerticalAlignment(SwingConstants.BOTTOM);
        title.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
        title.setFont(FontLib.getFont("Tahoma", Font.PLAIN, 16));
        title.setBackground(BACKGROUND);
        title.setForeground(FOREGROUND);
        */
        
        
        tview.addControlListener(new ControlAdapter() {
        	
        	public void itemClicked(VisualItem item, MouseEvent e) {
        		
                if ( item.canGetString(m_label) ) {
                	//title.setText(item.getString(m_label));
                	//highlightPanel(item.getString(m_label));
                	//m_view.stupidHackyFix();
                	//tview.getVis().repaint();
                	//tview.getVis().run("fullPaint");
                	//tview.getVis().run("animate");
                	tview.getVis().repaint();
                	
                	//m_view.repaint();
                } //if
        	} //itemReleased
        	
        	/*
            public void itemEntered(VisualItem item, MouseEvent e) {
            	// temporarily moved to itemReleased
            } //itemEntered
            
            public void itemExited(VisualItem item, MouseEvent e) {
                //title.setText(null);
            	//TODO restore this function later
            	//removePanelHighlight();
            } //itemExited
            */
        	
        	
        });
        
       
        
        /*
        Box box = new Box(BoxLayout.X_AXIS);
        box.add(Box.createHorizontalStrut(10));
        box.add(title);
        box.add(Box.createHorizontalGlue());
        box.add(searchPanel);
        box.add(Box.createHorizontalStrut(3));
        box.setBackground(BACKGROUND);
        */
        
        //JPanel panel = new JPanel(new BorderLayout());
        this.setLayout(new BorderLayout());
        this.setBackground(BACKGROUND);
        this.setForeground(FOREGROUND);
        
        if (!(tview == null)) {
        	this.add(tview, BorderLayout.CENTER);
        	//panel.repaint();
        } //if
        //panel.add(box, BorderLayout.SOUTH);
        
        //this.add(panel);
        myResize(m_overallSize);
        
        this.repaint();
        //this.setVisible(true);
        
        //endDisplayTime = System.nanoTime();
        //printSystemTimes();
        
    } //TreeView (constructor)
	
    /**
     * Sets the directory that we're exploring in looking for the INFO file
     * 
     * @param dir - selected directory
     */
   	public void setDirectory(String dir) {
    	
    	// Clear the existing m_dir
    	m_dir = null;
    	
    	// Find .info file in this directory or its parent
		File d = new File(dir);
		boolean infoFileFound = false;
		
		// Iterate across all files in the provided directory
		if (d.isDirectory()) {
			for (File child : d.listFiles()) {
				if (isINFOFile(child)) {
					infoFileFound = true;
					m_dir = child.getAbsolutePath();
					break;
				} //if
			} //for
		} //if
    	
		// If we haven't found the info file yet, step up a level and look again
    	if (!infoFileFound) {
    		d = new File(d.getParent());
			for (File child : d.listFiles()) {
				if (isINFOFile(child)) {
					infoFileFound = true;
					m_dir = child.getAbsolutePath();
					break;
				} //if
			} //for
    	} //if
    	
    	// Check to see if we found the new one
    	if (!infoFileFound) {
    		System.err.println("INFO file not found for timeline tree construction.");
    	} //if-else
    	
    } //setDirectory
    
   	/**
   	 * Checks to see if the current file under consideration is the project INFO file
   	 * 
   	 * @param child - file being checked
   	 * 
   	 * @return T/F
   	 */
	private boolean isINFOFile(File child) {
		String filepath = child.getPath();
		return ((filepath.substring(filepath.length()-5, filepath.length()).equals(".info")) || (filepath.substring(filepath.length()-5, filepath.length()).equals(".INFO")));
	} //isBNGLFile

	/**
	 * Reloads the tree with new structure/information
	 */
    public void reloadTree() {
    	int a = this.countComponents();
    	
    	this.remove(0);
    	
    	Tree t = null;
    	
    	// The actual data from the .INFO file
    	TimelineLoader loader = new TimelineLoader(m_dir);
    	ArrayList<TimelineItem> files = loader.parseFile();
    	String xml = loader.createXML(files);
    	printTimelineItems(files);
    	
    	// Load the data into a tree from the XML representation
        try {
           	String myPath = writeXMLToFile(xml, m_dir);
           	t = (Tree)new TreeMLReader().readGraph(myPath);

        } catch (Exception e) {
            e.printStackTrace();
            //System.exit(1);
        } //try-catch
            
        if (!(t == null)) {
         	// create a new treemap
        	tview = null;
           	tview = new MyTreeView(t, m_label, m_overallSize);
           	tview.setBackground(BACKGROUND);
           	tview.setForeground(FOREGROUND);
        } //if
        
        tview.addControlListener(new ControlAdapter() {
        	
        	public void itemClicked(VisualItem item, MouseEvent e) {
        		
                if ( item.canGetString(m_label) ) {
                	//title.setText(item.getString(m_label));
                	highlightPanel(item.getString(m_label));
                	//m_view.stupidHackyFix();
                	//tview.getVis().repaint();
                	tview.getVis().run("fullPaint");
                	tview.getVis().run("animate");
                	tview.getVis().repaint();
                	
                	//m_view.repaint();
                } //if
        	} //itemReleased
        	
        	/*
            public void itemEntered(VisualItem item, MouseEvent e) {
            	// temporarily moved to itemReleased
            } //itemEntered
            
            public void itemExited(VisualItem item, MouseEvent e) {
                //title.setText(null);
            	//TODO restore this function later
            	//removePanelHighlight();
            } //itemExited
            */
        	
        	
        });
        
       
        
        //JPanel panel = new JPanel(new BorderLayout());
        //this.setLayout(new BorderLayout());
        //this.setBackground(BACKGROUND);
        //this.setForeground(FOREGROUND);
        
        if (!(tview == null)) {
        	this.add(tview, BorderLayout.CENTER);
        	//panel.repaint();
        } //if
        //panel.add(box, BorderLayout.SOUTH);
        
        //this.add(panel);
        myResize(m_overallSize);
        
        //this.repaint();
        //this.setVisible(true);
        
        //endDisplayTime = System.nanoTime();
        //printSystemTimes();
    
    } //reloadTree
    
    
   /**
    * Writes XML to file
    * 
    * @param xml - XML representation of the tree
    * @param infoPath - path to the .INFO file
    * 
    * @return - path to the .XML file
    * @throws IOException
    */
    public String writeXMLToFile(String xml, String infoPath) throws IOException {
    	String xmlPath = getXMLPathFromInfoPath(infoPath);
    	
    	File file = new File(xmlPath);
    	//TODO: make this file hidden.  The below code works with Windows; it should be tested with Mac as well.  For Linux/Mac, rename the file to start with a "."
    	//Runtime.getRuntime().exec("attrib +H " + xmlPath);
    	
    	try {
    		if (!file.exists()) {
    			file.createNewFile();
    		} //if
    		
    		PrintWriter pw = new PrintWriter(file);
    		pw.write(xml);
    		pw.close();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	} //try-catch
    	
    	return xmlPath;
    } //writeXMLToFile
    
    /**
     * Creates the path to the XML file from the INFO file
     * 
     * @param infoPath - path to the .INFO file
     * 
     * @return - path to the .XML file
     */
    public String getXMLPathFromInfoPath(String infoPath) {
    	String xmlPath = infoPath;
    	int lastSlashPosition = infoPath.length();
    	
    	for (int i = infoPath.length(); i > 0; i--) {
    		if ((infoPath.substring(i-1, i).equals("\\")) || (infoPath.substring(i-1, i).equals("/"))) {
    			lastSlashPosition = i;
    			break;
    		} //if
    	} //for
    	
    	return xmlPath.substring(0, lastSlashPosition) + "info.xml";
    } //getXMLPathFromInfoPath
    
    /**
     * Class to hold the timeline tree
     * 
     * @author John
     */
    public class MyTreeView extends Display {

		private static final long serialVersionUID = -9158026561142479125L;

		/**
		 * Constructor:  renders the timeline tree visualization
		 * 
		 * @param t - the tree
		 * @param label - label field
		 * @param dim - max dimension
		 */
		public MyTreeView(Tree t, String label, Dimension dim) {
        	super(new Visualization());
    		
        	m_vis = new Visualization();
            m_vis.add(tree, t);
            
            m_nodeRenderer = new TextItemRenderer(label);
            m_nodeRenderer.setRenderType(ShapeRenderer.RENDER_TYPE_FILL);
            m_nodeRenderer.setHorizontalAlignment(Constants.LEFT);
            m_nodeRenderer.setRoundedCorner(8,8);
            
            //m_edgeRenderer = new EdgeRenderer(Constants.EDGE_TYPE_CURVE);
            m_edgeRenderer = new EdgeRenderer(Constants.EDGE_TYPE_LINE);
            
            DefaultRendererFactory rf = new DefaultRendererFactory(m_nodeRenderer);
            rf.add(new InGroupPredicate(treeEdges), m_edgeRenderer);
            m_vis.setRendererFactory(rf);
                   
            // colors
            ItemAction nodeColor = new NodeColorAction(treeNodes);
            ItemAction textColor = new ColorAction(treeNodes, VisualItem.TEXTCOLOR, ColorLib.rgb(0,0,0));
            m_vis.putAction("textColor", textColor);
            
            ItemAction edgeColor = new ColorAction(treeEdges, VisualItem.STROKECOLOR, ColorLib.rgb(200,200,200));
            
            // quick repaint
            ActionList repaint = new ActionList();
            repaint.add(nodeColor);
            repaint.add(new RepaintAction());
            m_vis.putAction("repaint", repaint);
            
            // full paint
            ActionList fullPaint = new ActionList();
            fullPaint.add(nodeColor);
            m_vis.putAction("fullPaint", fullPaint);
            
            // animate paint change
            ActionList animatePaint = new ActionList(400);
            animatePaint.add(new ColorAnimator(treeNodes));
            animatePaint.add(new RepaintAction());
            m_vis.putAction("animatePaint", animatePaint);
            
            // create the tree layout action
            // http://prefuse.org/doc/api/prefuse/action/layout/graph/NodeLinkTreeLayout.html
            // NodeLinkTreeLayout(java.lang.String group, int orientation, double dspace, double bspace, double tspace)
            NodeLinkTreeLayout treeLayout = new NodeLinkTreeLayout(tree, m_orientation, 50, 0, 8);
            //treeLayout.setLayoutAnchor(new Point2D.Double(25,300));
            treeLayout.setLayoutAnchor(new Point2D.Double(25, (m_overallSize.getHeight() / 2)));
            m_vis.putAction("treeLayout", treeLayout);
            
            // http://prefuse.org/doc/api/prefuse/action/layout/CollapsedSubtreeLayout.html
            // CollapsedSubtreeLayout(java.lang.String group, int orientation) 
            CollapsedSubtreeLayout subLayout = new CollapsedSubtreeLayout(tree, m_orientation);
            m_vis.putAction("subLayout", subLayout);
            
            AutoPanAction autoPan = new AutoPanAction();
            
            // create the filtering and layout
            ActionList filter = new ActionList();
            //TODO: show the full tree, not just the surrounding/top/bottom 4 layers
            filter.add(new FisheyeTreeFilter(tree, 4));
            filter.add(new FontAction(treeNodes, FontLib.getFont("Tahoma", 16)));
            filter.add(treeLayout);
            filter.add(subLayout);
            filter.add(textColor);
            filter.add(nodeColor);
            filter.add(edgeColor);
            m_vis.putAction("filter", filter);
            
            
            // animated transition
            ActionList animate = new ActionList(1000);
            animate.setPacingFunction(new SlowInSlowOutPacer());
            animate.add(autoPan);
            animate.add(new QualityControlAnimator());
            animate.add(new VisibilityAnimator(tree));
            animate.add(new LocationAnimator(treeNodes));
            animate.add(new ColorAnimator(treeNodes));
            animate.add(new RepaintAction());
            //animate.setDuration(10000);
            animate.setStepTime(1000);
            m_vis.putAction("animate", animate);
            m_vis.alwaysRunAfter("filter", "animate");
            m_vis.alwaysRunAfter("animate", "repaint");
           
            // create animator for orientation changes
            ActionList orient = new ActionList(2000);
            orient.setPacingFunction(new SlowInSlowOutPacer());
            orient.add(autoPan);
            orient.add(new QualityControlAnimator());
            orient.add(new LocationAnimator(treeNodes));
            orient.add(new RepaintAction());
            m_vis.putAction("orient", orient);
            
            // ------------------------------------------------
            
            // initialize the display
            //setSize(600, 700);
            setSize(dim);
            setItemSorter(new TreeDepthItemSorter());
            addControlListener(new ZoomToFitControl());
            addControlListener(new ZoomControl());
            addControlListener(new PanControl());
            addControlListener(new FocusControl(1, "filter"));
            
            /*
            registerKeyboardAction(
                new OrientAction(Constants.ORIENT_LEFT_RIGHT),
                "left-to-right", KeyStroke.getKeyStroke("ctrl 1"), WHEN_FOCUSED);
            
            registerKeyboardAction(
                new OrientAction(Constants.ORIENT_TOP_BOTTOM),
                "top-to-bottom", KeyStroke.getKeyStroke("ctrl 2"), WHEN_FOCUSED);
            
            registerKeyboardAction(
                new OrientAction(Constants.ORIENT_RIGHT_LEFT),
                "right-to-left", KeyStroke.getKeyStroke("ctrl 3"), WHEN_FOCUSED);
            
            registerKeyboardAction(
                new OrientAction(Constants.ORIENT_BOTTOM_TOP),
                "bottom-to-top", KeyStroke.getKeyStroke("ctrl 4"), WHEN_FOCUSED);
            */
        
            // filter graph and perform layout
            setOrientation(m_orientation);
            m_vis.run("filter");
          
            TupleSet search = new PrefixSearchTupleSet(); 
            m_vis.addFocusGroup(Visualization.SEARCH_ITEMS, search);
            
            search.addTupleSetListener(new TupleSetListener() {
                public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
                    m_vis.cancel("animatePaint");
                    m_vis.run("fullPaint");
                    m_vis.run("animatePaint");
                } //tupleSetChanged
            }); //addTupleSetListener

    	} //MyTreeView (constructor)
    	
		/**
		 * Resets the size of the TreeView
		 * 
		 * @param newSize - New size for the TreeView
		 */
    	public void resetSize(Dimension newSize) {
    		setSize(newSize);
    	} //setSize
   	
    	/**
    	 * Sets the orientation of the TreeView (top-down, bottom-up, left-right, right-left)
    	 * 
    	 * @param orientation - Orientation choice
    	 */
   	 	public void setOrientation(int orientation) {
   	        NodeLinkTreeLayout rtl = (NodeLinkTreeLayout)m_vis.getAction("treeLayout");
   	        CollapsedSubtreeLayout stl = (CollapsedSubtreeLayout)m_vis.getAction("subLayout");
   	        
   	        switch ( orientation ) {
   	        	case Constants.ORIENT_LEFT_RIGHT:
   	        		m_nodeRenderer.setHorizontalAlignment(Constants.LEFT);
   	        		m_edgeRenderer.setHorizontalAlignment1(Constants.RIGHT);
   	        		m_edgeRenderer.setHorizontalAlignment2(Constants.LEFT);
   	        		m_edgeRenderer.setVerticalAlignment1(Constants.CENTER);
   	        		m_edgeRenderer.setVerticalAlignment2(Constants.CENTER);
   	        		break;
   	        		
   	        	case Constants.ORIENT_RIGHT_LEFT:
   	        		m_nodeRenderer.setHorizontalAlignment(Constants.RIGHT);
   	        		m_edgeRenderer.setHorizontalAlignment1(Constants.LEFT);
   	        		m_edgeRenderer.setHorizontalAlignment2(Constants.RIGHT);
   	        		m_edgeRenderer.setVerticalAlignment1(Constants.CENTER);
   	        		m_edgeRenderer.setVerticalAlignment2(Constants.CENTER);
   	        		break;
   	        		
   	        	case Constants.ORIENT_TOP_BOTTOM:
   	        		m_nodeRenderer.setHorizontalAlignment(Constants.CENTER);
   	        		m_edgeRenderer.setHorizontalAlignment1(Constants.CENTER);
   	        		m_edgeRenderer.setHorizontalAlignment2(Constants.CENTER);
   	        		m_edgeRenderer.setVerticalAlignment1(Constants.BOTTOM);
   	        		m_edgeRenderer.setVerticalAlignment2(Constants.TOP);
   	        		break;
   	        		
   	        	case Constants.ORIENT_BOTTOM_TOP:
   	        		m_nodeRenderer.setHorizontalAlignment(Constants.CENTER);
   	        		m_edgeRenderer.setHorizontalAlignment1(Constants.CENTER);
   	        		m_edgeRenderer.setHorizontalAlignment2(Constants.CENTER);
   	        		m_edgeRenderer.setVerticalAlignment1(Constants.TOP);
   	        		m_edgeRenderer.setVerticalAlignment2(Constants.BOTTOM);
   	        		break;
   	        		
   	        	default:
   	        		throw new IllegalArgumentException("Unrecognized orientation value: "+orientation);
   	        		
   	        } //switch
   	        
   	        m_orientation = orientation;
   	        rtl.setOrientation(orientation);
   	        stl.setOrientation(orientation);
   	        
   	    } //setOrientation
   	    
   	 	/**
   	 	 * Returns the current orientation of the TreeView
   	 	 * 
   	 	 * @return - The current orientation of the TreeView
   	 	 */
   	    public int getOrientation() {
   	        return m_orientation;
   	    } //getOrientation
   	    
   	    /**
   	     * Inner class to set the orientation of the TreeView
   	     * 
   	     * @author John
   	     */
   	    public class OrientAction extends AbstractAction {
			private static final long serialVersionUID = -7076004984133408854L;
			private int orientation;
   	        
			/**
			 * Sets the orientation instance variable
			 * 
			 * @param orientation - Orientation choice
			 */
   	        public OrientAction(int orientation) {
   	            this.orientation = orientation;
   	        } //OrientAction (constructor)
   	        
   	        /**
   	         * Run the orientation updating actions
   	         *
   	         * @param evt - The ActionEvent trigger to change the orientation
   	         */
   	        public void actionPerformed(ActionEvent evt) {
   	            setOrientation(orientation);
   	            getVisualization().cancel("orient");
   	            getVisualization().run("treeLayout");
   	            getVisualization().run("orient");
   	        } //actionPerformed
   	        
   	    } //OrientAction (inner class)

   	    /**
   	     * Inner class to auto-pan the TreeView to the selected node
   	     * 
   	     * @author John
   	     */
   	    public class AutoPanAction extends Action {
   	        private Point2D m_start = new Point2D.Double();
   	        private Point2D m_end   = new Point2D.Double();
   	        private Point2D m_cur   = new Point2D.Double();
   	        private int     m_bias  = 150;
   	        
   	        /**
   	         * Runs the auto-pan action
   	         * 
   	         * @param frac
   	         */
   	        public void run(double frac) {
   	            TupleSet ts = m_vis.getFocusGroup(Visualization.FOCUS_ITEMS);
   	            if ( ts.getTupleCount() == 0 ) {
   	            	return;
   	            } //if
   	            
   	            if ( frac == 0.0 ) {
   	                int xbias=0, ybias=0;
   	                switch ( m_orientation ) {
   	                	case Constants.ORIENT_LEFT_RIGHT:
   	                		xbias = m_bias;
   	                		break;
   	                	
   	                	case Constants.ORIENT_RIGHT_LEFT:
   	                		xbias = -m_bias;
   	                		break;
   	                	
   	                	case Constants.ORIENT_TOP_BOTTOM:
   	                		ybias = m_bias;
   	                		break;
   	                	
   	                	case Constants.ORIENT_BOTTOM_TOP:
   	                		ybias = -m_bias;
   	                		break;
	                } //switch

    	            VisualItem vi = (VisualItem)ts.tuples().next();
    	            m_cur.setLocation(getWidth()/2, getHeight()/2);
    	            getAbsoluteCoordinate(m_cur, m_start);
    	            m_end.setLocation(vi.getX()+xbias, vi.getY()+ybias);
    	        } else {
    	            m_cur.setLocation(m_start.getX() + frac*(m_end.getX()-m_start.getX()), m_start.getY() + frac*(m_end.getY()-m_start.getY()));
    	            panToAbs(m_cur);
    	        } //if-else
    	    } //run
    	} //AutoPanAction (inner class)

   	    /**
   	     * Inner class to color the nodes
   	     * 
   	     * @author John
   	     */
    	public class NodeColorAction extends ColorAction {
    	        
    		/**
    		 * Constructor, calls the ColorAction constructor on the provided group
    		 * 
    		 * @param group - Nodes
    		 */
    	    public NodeColorAction(String group) {
    	        super(group, VisualItem.FILLCOLOR);
    	    } //NodeColorAction (constructor)
    	        
    	    /**
    	     * Returns the color of the given VisualItem
    	     * 
    		 * @param item - Get the color of this VisualItem 
    	     */
    	    public int getColor(VisualItem item) {
    	        if ( m_vis.isInGroup(item, Visualization.SEARCH_ITEMS) ) {
    	            return ColorLib.rgb(255,190,190);
    	        } else if ( m_vis.isInGroup(item, Visualization.FOCUS_ITEMS) ) {
    	            return ColorLib.rgb(198,229,229);
    	        } else if ( item.getDOI() > -1 ) {
    	            return ColorLib.rgb(164,193,193);
    	        } else {
    	            return ColorLib.rgba(255,255,255,0);
    	        } //if-else
    	    } //getColor
    	        
    	} //NodeColorAction (inner class)
 
    	public Visualization getVis() {
    		return m_vis;
    	} //getVis
    	
    } //MyTreeView (inner class)

    /**
     * Returns Visualization Object
     * 
     * @return - Visualization Object
     */
    public Visualization getMyVis() {
    	return tview.getVis();
    } //getMyVis
    
    /**
     * Returns TreeView object
     * 
     * @return - TreeView object
     */
    public MyTreeView getMyTreeView() {
    	return tview;
    } //getMyTreeView
    
    /**
     * Resize the ViewPart
     */
    public void myResize() {
    	myResize(m_overallSize);
    } //myResize
    
    /**
     * Resizes the ViewPart to the given dimensions
     * 
     * @param dimension - New size of the ViewPart
     */
	public void myResize(Dimension dimension) {
		m_overallSize = dimension;
		if (!(tview == null)) {
			
			// Have to do this!
			tview.resetSize(dimension);
			
			
			//tview.repaint();
			
			//tview.getVis().run("fullPaint");
			//tview.getVis().run("animatePaint");
			//tview.getVis().runAfter("animatePaint", "repaint");
			//tview.getVis().run("animatePaint");
			tview.getVis().runAfter("animate", "repaint");
			tview.getVis().run("filter");
			//tview.getVis().run("repaint");
			//tview.getVis().runAfter("repaint", "animatePaint");
			//tview.getVis().repaint();
			
			// Hardcoded delay workaround (TODO: temporary fix)
			for (int i = 0; i < 100000000; i++) {
				int j = i+1;
			} //for
			
			// Have to do this!
			tview.repaint();
		} //if
		
		this.setSize(dimension);
		
		//m_view.repaint();
		
	} //myResize

	/**
	 * Output the sequences of TimelineItems (for debugging purposes)
	 * 
	 * @param files - The set of all TimelineItems
	 */
	public void printTimelineItems(ArrayList<TimelineItem> files) {
		Iterator<?> iter = files.iterator();
		
		System.out.println("\n");
		
		while (iter.hasNext()) {
			TimelineItem item = (TimelineItem) iter.next();
			
			System.out.println("Filename: " + item.getName());
			System.out.println("Parent: " + item.getParent());
			System.out.println("Simulations:");
			
			if (item.getSimulations().isEmpty()) {
				System.out.println("  - NONE!");
			} else {
				Iterator<?> simIter = item.getSimulations().iterator();
				
				while (simIter.hasNext()) {
					String sim = (String) simIter.next();
					System.out.println("  - " + sim);
				} //while
			} //if-else
			
			System.out.println("");
		} //while
		
		System.out.println("End of TimelineItem list.");
				
	} //printTimelineItems
	/*
	public void setSelection(String sel) {

		TupleSet search = new PrefixSearchTupleSet();
		
		
		
		tview.getVis().addFocusGroup(Visualization.SEARCH_ITEMS, search);
		
	} //setSelection
	*/
	
	/**
	 * Pass a message to the SMPanel to highlight/select a model
	 * 
	 * @param modelName - model to select
	 */
	public void highlightPanel(final String modelName) {
		
		// Find the TimelineView to pass a selection message to
		org.eclipse.swt.widgets.Display.getDefault().asyncExec(new Runnable() {
		    @Override
		    public void run() {
		        IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		        
				SmallMultiplesView view = (SmallMultiplesView) getView(iw,  "rulebender.simulationjournaling.view.smallmultiplesview");
				
				if (view != null) {
					Message msg = new Message();
					msg.setType("ModelSelection");
					msg.setDetails(modelName);
					
					view.iGotAMessage(msg);
				} else {
					System.err.println("Could not find TimelineView to pass message.");
				} //if-else
		    } //run
		});
		
	} //highlightPanel
	/*
	public void removePanelHighlight() {
		
		// Find the TimelineView to pass a selection message to
		org.eclipse.swt.widgets.Display.getDefault().asyncExec(new Runnable() {
		    @Override
		    public void run() {
		        IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		        
				SmallMultiplesView view = (SmallMultiplesView) getView(iw,  "rulebender.simulationjournaling.view.smallmultiplesview");
				
				if (view != null) {
					Message msg = new Message();
					msg.setType("ModelDeselection");
					msg.setDetails("");
					
					view.iGotAMessage(msg);
				} else {
					System.err.println("Could not find TimelineView to pass message.");
				} //if-else
		    } //run
		});
		
	} //removePanelHighlight
	*/
	
	public static IViewPart getView(IWorkbenchWindow window, String viewId) {
	    IViewReference[] refs = window.getActivePage().getViewReferences();
	    for (IViewReference viewReference : refs) {
	        if (viewReference.getId().equals(viewId)) {
	            return viewReference.getView(true);
	        } //if
	    } //for
	    return null;
	} //getView
	
	
	/*
	public void printSystemTimes() {
		System.out.println("Start time:       " + startTime);
		System.out.println("End parser time:  " + endParserTime);
		System.out.println("End display time: " + endDisplayTime);
		System.out.println("");
		System.out.println("Parser Time:      " + (endParserTime - startTime));
		System.out.println("Display Time:     " + (endDisplayTime - endParserTime));
	} //printSystemTimes
    */
	/*
    public void registerObserver(TimelineItemSelectionListener observer) {
        observers.add(observer);
    } //registerObserver
    
    public void notifyListeners() {
        for(TimelineItemSelectionListener observer : observers) {
            observer.notify("myModel");
        } //for
    } //notifyListeners
    
    public void aTimelineItemWasSelected() {
        //bounce etc
        notifyListeners();
    } //aTimelineItemWasSelected
	*/
} //TreeView