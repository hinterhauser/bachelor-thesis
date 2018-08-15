set terminal jpeg
txt = '.txt'
jpg = '.jpg'
set output ARG1.jpg
set yrange [0:1.1]
set xrange [0:11]
plot 	ARG1.txt using 1:2 title 'MDL' with linespoints, \
	ARG1.txt using 1:3 title 'Kmean' with linespoints, \
	ARG1.txt using 1:4 title 'DBSCAN' with linespoints, \
	ARG1.txt using 1:5 title 'hierarchical' with linespoints