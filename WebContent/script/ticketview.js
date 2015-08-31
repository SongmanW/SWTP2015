/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
window.onload = function(){
	//prepare "Type"-inputfield and display
	if('${t1.type}' == "feature"){
		setchecked('type_selection',"feature");
	    document.getElementById('estimated_time_display_span').style.display = "inline";
	    document.getElementById('estimated_time_change_span').style.display = "inline"}
	else  {document.getElementById('estimated_time_display_span').style.display = "none";
	document.getElementById('estimated_time_change_span').style.display = "none";}
	if('${t1.type}' == "bug"){
		setchecked('type_selection',"bug");;
	}
	//prepare "Responsible User"-inputfield
	setchecked('responsible_user_selection',"${t1.responsible_user}");
	//prepare "State"-inputfield
	setchecked('state_selection',"${t1.status}");
};

