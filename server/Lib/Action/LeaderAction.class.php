<?php
//If garbled, uncomment this line.
header("Content-Type: text/html; charset=utf-8");

class LeaderAction extends Action {
	
	/**
	 * If the user is a leader, return the list in his group
	 */
	public function getUserList($usrid){
		if(!isLeader($usrid)){
			return false;
		}
		
		$user = getUser($usrid);
		$model = M("user");
		$usrls = $model -> where('partyid = '.$user['partyid']) -> field('usrid, username') -> select();
		
		$this -> ajaxReturn($usrls, "json");
	}
	
	
	/**
	 * add news to the party
	 */
	public function addNews($usrid = 0,$title, $content){
		if($usrid == 0){
			$toPartyid = 0;
		}else if(!isLeader($usrid)){
			echo "false";
			return false;
		}else{
			$user = getUser($usrid);
			$toPartyid = $user['partyid'];
		}
		
		try{
			$model = M("news");
			$data['toPartyid'] = $toPartyid;
			$data['title'] = $title;
			$data['content'] = $content;
			$model -> data($data) -> add();
			echo "true";
			return true;
		}catch(Exception $e){
			echo "false";
			return false;
		}
		
	}

	/**
	 * add new user
	 */
	public function addUser($usrid, $newUser, $name){
		if (!preg_match("/^\d{11}$/",$newUser)){
			echo "false";
			return false;
		}

		$user = getUser($usrid);
		$partyid = $user['partyid'];

		// echo $partyid;

		try{
			$model = M("user");
			$data['usrid'] = $newUser;
			$data['partyid'] = $partyid;
			$data['username'] = $name;
			
			$model -> data($data) -> add();
			//echo $model -> getlastSql().'<br/>';
			echo "true";
			return true;
		}catch(Exception $e){
			//echo "false";
			return false;
		}
	}

	/* modify the user's information */
	public function updateUser($usrid, $key, $value){
		$isLeader = isLeader($usrid);
		$party = getParty(getUser($usrid));

		if($key == "status" && $value == 4){
			/* chose the new assistant for the party */
			echo changeLeader($party['partyid'],"assistant", $usrid);
			return;
		}

		$model = M("user");
		$model->startTrans();

		$condition["usrid"] = $usrid;

		$data[$key] = $value;

		$result= $model -> where($condition) -> data($data) -> save();

		if($result != false){
			// If the user is a leader but he go to another party
			if($isLeader){
				$partymodel = M("party");
				$partycondition = $party["partyid"];

				if($party["leader"] == $usrid){
					$partydata["leader"] = "";
				}else{
					$partydata["assistant"] = "";
				}

				$partyresult= $partymodel -> where($partycondition) -> data($partydata) -> save();
				$partymodel -> getlastSql();

				if($partyresult == false){
					echo "false";
					$model->rollback();
					return false;
				}
			}
			echo 'true';
		}else{
			echo 'false';
		}

		$model->commit(); 
	}

	public function searchParty($partyname){
		$party = findPartyByName($partyname);
		echo $party['partyid'];
	}


}