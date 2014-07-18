#partyapp

An Android app for the party in FDU. It uses customer/server model to build this application.

##server
It uses thinkPHP struct to construct the server. When starting the server, customer-end can use 
URL   
`<server-path>\index.php\<class-name>\<function-name>\<parameter-key>\<parameter-value>`   
to call the service.

All the data are transported via **HTTP** protocol in **JSON** struct.

###Index
This class provides news view service, which can be asked anonymous. 

####index(usrid = 0, start = 0)
This method returns news id, title and date in JSON ARRAY from *start* number
 to *start + length* number (length is 20 in default). 
All returned news are public or private in the user's group. If usrid is 0, 
the news are all public for all people.

    e.g.
    invoke http://<server-path>/index.php/Index/index/usrid/1/start/1
    return
    [{"id":"9","title":"\u4eca\u9519","date":"2014-07-09 13:48:03"},{"id":"7","title":"\u6211\u5f71","date":"2014-07-09 13:45:28"}]
    
###getNews(newsid)
This method returns the detail of a target news which id is *newsid*. The data contains
**title**, **date** and **content**.

##User
This class provides common service for a single logined user.

###login(usr, pwd)
If login successfully, it returns string *true*, else *false*.

###getUserInfo(usrid)
It returns a JSON ARRAY contains user information with following values:  

* usrid
* username
* status // the level's id of the user in the party
* invoke_date // when the user arrive the status
* partyid // the party's id of the user
* level
* party // name of the party
* submit_date // when the user should summit his paper next time

###nextSubmit(usrid)
return a string for next time the user should summit his papper

###getCmt(start = 0)
Return comment JSON ARRAY in chat room from *start* index in *length* length

###pushCmt(usrid, cmt)
Add a comment in chat room for user whose id is usrid.

###isLeader(usrid)
Whether the user should take leader privilege. It will return true or false string.

##Leader
This class provide service for leader level. That is, all the function will check whether the user could ask for the service and return false if not.

###getUserList(usrid)
Returns a JSON ARRAY contains the list of all users in his group. User information contains user id and name.

###updateUser(usrid, key, value)
Update a parameter *key* with value *value* for a target user *usrid*. If successed, return STRING true, else false.

###addUser(usrid, newUser, name)
A leader whose id is usrid adds a new user in his group. It should provide the new user's id and username as parameters newUser and name.

###addNews(usrid = 0, title, content)
Add a news in user's group, whose id is usrid. If the usrid is 0, the news should be public for all users.
