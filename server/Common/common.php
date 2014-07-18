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

	/** 
	 * return the next time the user should submit his paper.
	 * 
	 * status 1 should submit per 3 monthes
	 * status 2 should submit per 6 monthes
	 */
	function nextSubmit($usrid){
		$user = getUser($usrid);
		/* should it submit papers? If not, return false */
		switch ($user['status']) {
			case 1:
				$length = 3;
				break;
			case 2:
				$length = 6;
				break;
			default:
				return false;
		}

		/* get params for invoke date and current date */
		$cur_date = date("Y-m-d");
		$cur_year = date("Y");
		$cur_month = substr($cur_date, 5, 2);
		$invoke_date = $user['invoke_date'];
		$invoke_month = substr($invoke_date, 5, 2);
		$invoke_day = substr($invoke_date, 8,2);

		/* get the right month */
		for($i = 0; $invoke_month < $cur_month; $invoke_month += $length);
		if($invoke_month > 12){
			$cur_year += 1;
			$invoke_month -= 12;
		}

		$date = mktime(23,59,59, $invoke_month,$invoke_day, $cur_year);
		

		// echo date("Y-m-d",$date)."<br/>";
		// echo $cur_date."<br/>";
		if($date < time()){
			$invoke_month += $length;
			if($invoke_month > 12){
				$cur_year += 1;
				$invoke_month -= 12;
			}
			$date = mktime(23,59,59, $invoke_month,$invoke_day, $cur_year);
		}

		return date("Y-m-d",$date);
	}
?>