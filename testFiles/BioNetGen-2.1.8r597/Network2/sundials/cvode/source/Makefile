# -----------------------------------------------------------------
# $Revision: 1.9.2.2 $
# $Date: 2005/02/14 23:14:54 $
# -----------------------------------------------------------------
# Programmer(s): Radu Serban and Aaron Collier @ LLNL
# -----------------------------------------------------------------
# Copyright (c) 2002, The Regents of the University of California.
# Produced at the Lawrence Livermore National Laboratory.
# All rights reserved.
# For details, see sundials/cvode/LICENSE.
# -----------------------------------------------------------------
# Makefile for CVODE module
#
# cvode/source/Makefile.  Generated from Makefile.in by configure.
# -----------------------------------------------------------------

SHELL = /bin/sh

srcdir       = .
builddir     = .
abs_builddir = /Users/mr_smith22586/Documents/workspace/rulebender/distributionResources/BioNetGen/mac64/BioNetGen-2.1.8r597/Network2/sundials/cvode/source
top_builddir = ../../
prefix       = /Users/mr_smith22586/Documents/workspace/rulebender/distributionResources/BioNetGen/mac64/BioNetGen-2.1.8r597/Network2
exec_prefix  = ${prefix}
includedir   = ${prefix}/include
libdir       = ${exec_prefix}/lib

INSTALL        = /usr/bin/install -c
INSTALL_LIB    = ${INSTALL}
INSTALL_HEADER = ${INSTALL} -m 644

LIBTOOL      = $(SHELL) $(top_builddir)/libtool
LIBTOOL_DEPS = config/ltmain.sh

CPP      = /usr/bin/cc -E
CPPFLAGS = 
CC       = /usr/bin/cc
CFLAGS   = -g -O2
LDFLAGS  = 
LIBS     = -lm 

SHARED_INC_DIR   = $(srcdir)/../../shared/include
CVODE_INC_DIR    = $(srcdir)/../include

CVODE_LIB       = libsundials_cvode.la
CVODE_LIB_FILES = cvode.lo cvodeio.lo cvband.lo cvdense.lo cvdiag.lo cvspgmr.lo cvbandpre.lo cvbbdpre.lo

OBJECT_FILES = cvode.o cvodeio.o cvband.o cvdense.o cvdiag.o cvspgmr.o cvbandpre.o cvbbdpre.o

all: update $(CVODE_LIB_FILES)

update:
	${INSTALL_HEADER} ${top_builddir}/config/sundials_config.h ${builddir}

install: update $(CVODE_LIB_FILES)
	$(LIBTOOL) --mode=link $(CC) $(CFLAGS) -o $(CVODE_LIB) $(CVODE_LIB_FILES) -rpath $(libdir) $(LDLFAGS) $(LIBS) -avoid-version
	$(LIBTOOL) --mode=install $(INSTALL_LIB) $(CVODE_LIB) $(libdir)
	$(INSTALL_HEADER) $(CVODE_INC_DIR)/cvode.h $(includedir)
	$(INSTALL_HEADER) $(CVODE_INC_DIR)/cvdense.h $(includedir)
	$(INSTALL_HEADER) $(CVODE_INC_DIR)/cvband.h $(includedir)
	$(INSTALL_HEADER) $(CVODE_INC_DIR)/cvdiag.h $(includedir)
	$(INSTALL_HEADER) $(CVODE_INC_DIR)/cvspgmr.h $(includedir)
	$(INSTALL_HEADER) $(CVODE_INC_DIR)/cvbandpre.h $(includedir)
	$(INSTALL_HEADER) $(CVODE_INC_DIR)/cvbbdpre.h $(includedir)

clean:
	$(LIBTOOL) --mode=clean rm -f $(CVODE_LIB)
	rm -f $(CVODE_LIB_FILES)
	rm -f $(OBJECT_FILES)
	rm -f sundials_config.h

uninstall: clean
	$(LIBTOOL) --mode=uninstall rm -f $(libdir)/$(CVODE_LIB)
	rm -f $(includedir)/cvode.h
	rm -f $(includedir)/cvdense.h
	rm -f $(includedir)/cvband.h
	rm -f $(includedir)/cvdiag.h
	rm -f $(includedir)/cvspgmr.h
	rm -f $(includedir)/cvbandpre.h
	rm -f $(includedir)/cvbbdpre.h

cvode.lo: $(srcdir)/cvode.c
	$(LIBTOOL) --mode=compile $(CC) $(CPPFLAGS) -I. -I$(SHARED_INC_DIR) -I$(CVODE_INC_DIR) $(CFLAGS) -c $(srcdir)/cvode.c
cvodeio.lo: $(srcdir)/cvodeio.c
	$(LIBTOOL) --mode=compile $(CC) $(CPPFLAGS) -I. -I$(SHARED_INC_DIR) -I$(CVODE_INC_DIR) $(CFLAGS) -c $(srcdir)/cvodeio.c
cvdense.lo: $(srcdir)/cvdense.c
	$(LIBTOOL) --mode=compile $(CC) $(CPPFLAGS) -I. -I$(SHARED_INC_DIR) -I$(CVODE_INC_DIR) $(CFLAGS) -c $(srcdir)/cvdense.c
cvband.lo: $(srcdir)/cvband.c
	$(LIBTOOL) --mode=compile $(CC) $(CPPFLAGS) -I. -I$(SHARED_INC_DIR) -I$(CVODE_INC_DIR) $(CFLAGS) -c $(srcdir)/cvband.c
cvdiag.lo: $(srcdir)/cvdiag.c
	$(LIBTOOL) --mode=compile $(CC) $(CPPFLAGS) -I. -I$(SHARED_INC_DIR) -I$(CVODE_INC_DIR) $(CFLAGS) -c $(srcdir)/cvdiag.c
cvspgmr.lo: $(srcdir)/cvspgmr.c
	$(LIBTOOL) --mode=compile $(CC) $(CPPFLAGS) -I. -I$(SHARED_INC_DIR) -I$(CVODE_INC_DIR) $(CFLAGS) -c $(srcdir)/cvspgmr.c
cvbandpre.lo: $(srcdir)/cvbandpre.c
	$(LIBTOOL) --mode=compile $(CC) $(CPPFLAGS) -I. -I$(SHARED_INC_DIR) -I$(CVODE_INC_DIR) $(CFLAGS) -c $(srcdir)/cvbandpre.c
cvbbdpre.lo: $(srcdir)/cvbbdpre.c
	$(LIBTOOL) --mode=compile $(CC) $(CPPFLAGS) -I. -I$(SHARED_INC_DIR) -I$(CVODE_INC_DIR) $(CFLAGS) -c $(srcdir)/cvbbdpre.c


libtool: $(top_builddir)/$(LIBTOOL_DEPS)
	@cd $(top_builddir) ;                 \
	 $(SHELL) ./config.status --recheck ; \
	 cd $(abs_builddir)
