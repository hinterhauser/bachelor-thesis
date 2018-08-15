function NMI_hier
  
  #for i=6:10
    #ANMI(int2str(5), int2str(i), "near", "hier");
    #ANMI(int2str(5), int2str(i), "over", "hier");
    #ANMI(int2str(5), int2str(i), "arbitrary", "hier");
    #ANMI(int2str(5), int2str(i), "moons", "hier");
    #ANMI(int2str(5), int2str(i), "elliptical", "hier");
  #endfor
  
  for i=5:5:50
    ANMI(int2str(i), int2str(1), "near", "hier");
    ANMI(int2str(i), int2str(1), "over", "hier");
    ANMI(int2str(i), int2str(1), "arbitrary", "hier");
    ANMI(int2str(i), int2str(1), "moons", "hier");
    ANMI(int2str(i), int2str(1), "elliptical", "hier");
  endfor
  
endfunction