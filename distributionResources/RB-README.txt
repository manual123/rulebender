RuleBender is licensed under the Eclipse Public License v 1.0 and 
includes the BioNetGen (BNG) modeling and simulation package along 
with the NFsim simulator.

For more information:
---------------------
RuleBender: rulebender.org 
BioNetGen: bionetgen.org 
NFsim: nfsim.org

All source codes are available at http://github.com/RuleWorld.

BioNetGen source code is packaged with RuleBender in the 
'BioNetGen-2.2.6' directory. All necessary binary files are included in 
the 'BioNetGen-2.2.6/bin' directory.

For NFsim, the binary is included in the 'BioNetGen-2.2.6/bin' directory. 
The source code is included separately in 'NFsim_v1.11-src'. If you want 
to replace this packaged version, you must replace the NFsim executables 
in 'BioNetGen-2.2.6/bin'.

######################################################################

BioNetGen Credits:

The following people have contributed to BioNetGen code development:

Michael L. Blinov <blinov@uchc.edu>
   * Original author.
   * Testing and development of new ideas.
   * Developed interface to Virtual Cell.

James R. Faeder <faeder@lanl.gov>
   * Primary BioNetGen developer since version 1.0.
   * Testing and development of new ideas.
   * Code maintainer.
   * Current project co-PI.

William S. Hlavacek <wish@lanl.gov>
   * Driving force in the development.
   * Testing and development of new ideas.
   * Current project PI.

Byron Goldstein <bxg@lanl.gov>
   * Pioneered concept of signaling models based on multivalent interactions.
   * Original project PI.

Justin Hogg <justinshogg@gmail.com>
   * Compartmental BNGL (cBNGL).
   * General maintenance.

Leonard Harris <lh64@cornell.edu>
   * Network3 development: global functions, accelerated stochastics (PLA)
   * General maintenance.

Ilya Korsunsky
   * Initial deployment of global functions for Network3.

John Sekar <johnarul.sekar@gmail.com>
   * Model visualization methods.

Jose Juan Tapia <jjtapia@pitt.edu>
   * BioNetGen grammars development and implementation
   * SBML2BNGL developer
      
Michael Sneddon <michael.sneddon@yale.edu>
   * Author and maintainer of NFsim.
   * Contributor to BNGXML and maintainer of BNG-NFsim interface.      

Thierry Emonet <thierry.emonet@yale.edu>
   * NFsim PI 
   
Jeremy Kozdon
   * Code for use of sparse Jacobian in CVODE calls, which greatly accelerates
ODE integration for large networks (sparse=>1 option).

Nathan Lemons
   * HNauty code for canonical labeling of hierarchical graphs, which is (optionally) used
in network generation,.

Matthew Fricke <matthew@gofigure.org>
   * Primary author of RuleBuilder.

Leigh Fanning <leigh@verseira.net>
   * Contributor to RuleBuilder development.
   * Developed RuleBuilder help system.

Robert Seletsky <robert1unm@yahoo.com>
   * Testing of BioNetGen and RuleBuilder.

Sarah Faeder <smgfaeder@gmail.com>
   * Contributor of PhiBPlot.
   * Testing and bug fixes for RuleBuilder.

Nikolay Borisov <nikolay.borisov@jefferson.edu>
   * Primary developer of Macro module.
   * Testing of BioNetGen.

Mikhail Kravchenko <krmisha@rambler.ru>
   * Implementation of macro code.
   * Streamlining of BioNetGen interface.

Alexander S. Chistopolsky <eroxi@savelovo.net>
   * Developer of macro algorithm

Marc Birtwistle <mrbirdy@udel.edu>
   * Contributor of parameter sensitivity code.

######################################################################

NFSim Credits:

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                   %
%     @@    @  @@@@@                %
%     @ @   @  @                    %
%     @  @  @  @@@@  ___            %
%     @   @ @  @    /__  | |\ /|    %
%     @    @@  @    ___\ | | v |    %
%                                   %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

NFsim - the network free stochastic simulator, v1.11

michael w. sneddon
justin s. hogg
james r. faeder
thierry emonet

Yale University
University of Pittsburgh
funded by the National Science Foundation

