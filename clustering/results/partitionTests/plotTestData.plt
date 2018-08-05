set datafile separator ','
set xrange [0:100]
set yrange [0:100]
set xtics 100.0/ARG1
set ytics 100.0/ARG1
set grid xtics lt 1 lc black
set grid ytics lt 1 lc black
set grid
plot ARG2 with points using 1:2