function NMI_kmean
  
  #for i=1:10
    #ANMI(int2str(5), int2str(i), "near", "kmean");
    #ANMI(int2str(5), int2str(i), "over", "kmean");
    #ANMI(int2str(5), int2str(i), "arbitrary", "kmean");
    #ANMI(int2str(5), int2str(i), "moons", "kmean");
  #endfor
  
  for i=5:5:50
    ANMI(int2str(i), int2str(1), "near", "kmean");
    ANMI(int2str(i), int2str(1), "over", "kmean");
    ANMI(int2str(i), int2str(1), "arbitrary", "kmean");
    ANMI(int2str(i), int2str(1), "moons", "kmean");
  endfor
  
endfunction