<?php
//If garbled, uncomment this line.
header("Content-Type: text/html; charset=utf-8");

class IndexAction extends Action {

	/**
	 * Show the list of news in the group for a specific user. If the user's id is 0, show the public news only.
	 */
    public function index($usrid = 0, $start = 0){
		
		/*****************************
		 *****************************
		 * return $length title only *
		 *****************************
		 *****************************/
		$length = 20; 
		
		/* format sql query string */
		$model = M('news');		
		$sqlstr = "`toPartyid` = 0";
		if($usrid != 0){
			/* add the user's party id */
			$user = getUser($usrid);			
			$sqlstr = $sqlstr." OR `toPartyid` = ".$user['partyid'];
		}
		
		$newsArray = $model -> field('id,title,date') -> where($sqlstr) -> limit($start, $length) -> order("id desc")-> select() ;
		
		//echo $model -> getlastSql().'<br/>';
		//foreach($newsArray as $news){
		//	echo "<h1>".$news['title']."</h1>";
		//	echo $news['content']."<br/>";	
		//}
		
		$this -> ajaxReturn($newsArray,"json");
    }
	
	/**
	 * Get a target news according to the id.
	 */
	public function getNews($newsid){
		$model = M("news");
		$newsArray = $model -> field('title, date, content') -> where('id = '.$newsid) -> select();
		
		try{
			$this -> ajaxReturn($newsArray[0], "json");
		}catch(Exception $e){
			return NULL;
		}
	}


}