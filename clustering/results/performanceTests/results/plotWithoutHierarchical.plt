set terminal jpeg
txt = '.txt'
jpg = '.jpg'
set output ARG1.jpg
set xrange [0:11]
set yrange [0:29000]
plot 	ARG1.txt using 1:2 title 'MDL' with linespoints, \
	    ARG1.txt using 1:3 title 'Kmean' with linespoints, \
	    ARG1.txt using 1:4 title 'DBSCAN' with linespoints