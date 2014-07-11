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
			return true;
		}catch(Exception $e){
			return false;
		}
		
	}


}