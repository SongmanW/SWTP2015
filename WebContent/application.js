/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function showSpan(elem){
   if(elem.value == "feature")
      document.getElementById('estimated_time_change_span').style.display = "inline";
   else document.getElementById('estimated_time_change_span').style.display = "none";
}

function setchecked(selectid,valuewert)
{
  optionen=document.getElementById(selectid).options;
  for(i=0;i<optionen.length;i++)
  {
    if(optionen[i].value==valuewert)
	{
	  optionen[i].setAttribute('selected','selected');
	}
  }
}