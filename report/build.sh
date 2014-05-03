#! /bin/bash
module add texlive
pdflatex FYP.tex
bibtex FYP.aux
pdflatex FYP.tex
pdflatex FYP.tex
acroread FYP.pdf
