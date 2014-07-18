<?php
//If garbled, uncomment this line.
header("Content-Type: text/html; charset=utf-8");

class UserAction extends Action {
	/**
	 * Login to the system. return a boolean value.
	 */
	public function login($usr='', $pwd=''){
		//echo "hi".$pwd;
		$model = M('usr_pwd');
		$user = $model -> where('usrid = '.$usr.' AND pwd = '.$pwd) -> find();
		
		//echo $model -> getlastSql().'<br/>';
		
		if($user == NULL){
			echo "false";
			return false;
		}else{
			echo "true";
			return true;
		}
	}
	

	/** 
	 * return the next time the user should submit his paper.
	 * 
	 * status 1 should submit per 3 monthes
	 * status 2 should submit per 6 monthes
	 */
	public function nextSubmit($usrid){
		echo nextSubmit($usrid);
	}

	/**
	 * get the user's information
	 */
	public function getUserInfo($usrid){
		$user = getUser($usrid);
		$party = getParty($user);
		
		switch ($user['status']) {
			case 0:
				$user['level'] = "群众";
				break;
			case 1:
				$user['level'] = "积极分子";
				break;
			case 2:
				$user['level'] = "预备党员";
				break;
			case 3:
				$user['level'] = "党员";
				break;
		}

		$user['party'] = $party['partyname'];
		
		if($party['leader'] == $usrid){
			$user['level'] = '党支部书记';
		}else if($party['assistant'] == $usrid){
			$user['level'] = '党支部副书记';
		}

		$user['submit_date'] = nextSubmit($usrid);
		//foreach($user as $e){
		//	echo $e."<br/>";
		//}
		
		$this -> ajaxReturn($user,"json");
	}
	
	/**
	 * Show the comments
	 */
	public function getCmt($start = 0){
		/*****************************
		 *****************************
		 * return $length title only *
		 *****************************
		 *****************************/
		$length = 20; 
		
		/* format sql query string */
		$model = M("comment");
		$cmtArr = $model -> field('`comment`.`userid`,`user`.`username`,`comment`.`content`')
			-> join('INNER JOIN `user` ON `comment`.`userid` = `user`.`usrid`') 
			-> limit($start, $length) -> order("id desc") -> select() ;
		
		//echo $model -> getlastSql().'<br/>';
		//foreach($cmtArr as $cmt){
			//echo "<h1>".$cmt['userid']."&nbsp;".$cmt['username']."</h1>";
			//echo $cmt['content']."<br/>";	
		//}
		
		$this -> ajaxReturn($cmtArr,"json");
		
	}
	
	/**
	 * add a comment and return a boolean value to make sure whether it comments successfully
	 */
	public function pushCmt($usrid, $cmt){
		try{
			$model = M("comment");
			$data['userid'] = $usrid;
			$data['content'] = $cmt;
			$model -> data($data) -> add();
			return true;
		}catch(Exception $e){
			return false;
		}
	}

	/**
	 * If the user is not a leader, return false.
	 */
	public function isLeader($usrid){
		if(isLeader($usrid)){
			echo "true";
		}else{
			echo "false";
		}
	}

}