function NMI_mdl
  
  for i=1:10
    ANMI(int2str(5), int2str(i), "near", "mdl");
    ANMI(int2str(5), int2str(i), "over", "mdl");
    ANMI(int2str(5), int2str(i), "arbitrary", "mdl");
    ANMI(int2str(5), int2str(i), "moons", "mdl");
    ANMI(int2str(5), int2str(i), "elliptical", "mdl");
  endfor
  
  for i=5:5:50
    ANMI(int2str(i), int2str(1), "near", "mdl");
    ANMI(int2str(i), int2str(1), "over", "mdl");
    ANMI(int2str(i), int2str(1), "arbitrary", "mdl");
    ANMI(int2str(i), int2str(1), "moons", "mdl");
    ANMI(int2str(i), int2str(1), "elliptical", "mdl");
  endfor
  
endfunction