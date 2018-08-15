function NMI_dbscan
  
  #for i=1:10
    #ANMI(int2str(5), int2str(i), "near", "dbscan");
    #ANMI(int2str(5), int2str(i), "over", "dbscan");
    #ANMI(int2str(5), int2str(i), "arbitrary", "dbscan");
    #ANMI(int2str(5), int2str(i), "moons", "dbscan");
    #ANMI(int2str(5), int2str(i), "elliptical", "dbscan");
  #endfor
  
  for i=5:5:50
    ANMI(int2str(i), int2str(1), "near", "dbscan");
    ANMI(int2str(i), int2str(1), "over", "dbscan");
    ANMI(int2str(i), int2str(1), "arbitrary", "dbscan");
    ANMI(int2str(i), int2str(1), "moons", "dbscan");
    ANMI(int2str(i), int2str(1), "elliptical", "dbscan");
  endfor
  
endfunction