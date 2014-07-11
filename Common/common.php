<?php
	function getUser($usrid){
		$usermodel = M('user');
		 return $user = $usermodel -> where("usrid = ".$usrid) -> find();
		
		//echo $usermodel -> getlastSql();
	}
	
	function getParty($user){
		$model = M('party');
		return $party = $model -> where("partyid = ".$user['partyid']) -> find();
	}
	
	function isLeader($usrid){
		$usr = getUser($usrid);
		$party = getParty($usr);
		
		if($party['leader'] == $usrid || $party['assistant'] == $usrid){
			return true;
		}else{
			return false;
		}
	}
?>