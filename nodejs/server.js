var express = require('express');
var mysql = require('mysql');

var app = express();
	var connection = mysql.createConnection({
		host	: 'localhost',
		query	: { pool : true },	
		user	: 'owen',
		password : 'owen',
		database : 'osam'
	});


//로그인
app.get('/logincheck', function(request, response){
	var loginSql = 'SELECT num,id FROM wb_profile WHERE id=' + request.query.id + '&& pw=' + request.query.pw;
	var jsondate;
	connection.query(loginSql, function (error, rows, fields) {
		if (error){
			response.sendStatus(400);
			return;
		}
	 	if(rows.length == 0){
	 		jsondate= '{"num":"not","id":"not","result":"false"}';
	 		response.send(jsondate);
	 		response.end();
	 	}else{
	 		jsondate= '{"num":"' + rows[0].num;
	 		jsondate= jsondate + '","id":"' + rows[0].id + '","result":"true"}';
	 		response.send(jsondate);
	 		response.end();
	 	}	
	});
});




//ID체크
app.get('/checkid', function(request, response){
		var likeSql = ' \
						SELECT id	\
						FROM wb_profile\
						WHERE id= ' +request.query.id;

		connection.query(likeSql, function (error, rows, fields) {
				if (error){
					response.sendStatus(400);
					return;
				}
			 	if(rows.length == 0){
			 		jsondate= '{"checkid":"good","title":"not","likenum":"not"}';
			 		response.send(jsondate);
			 		response.end();
			 	}else{
			 		jsondate= '{"checkid":"not","title":"not","likenum":"not"}';
			 		response.send(jsondate);
			 		response.end();
			 	}	
		 });
});





//회원등록
app.get('/regi', function(request, response){
		var likeSql = ' \
						INSERT INTO wb_profile	\
						(id,pw,name)\
						VALUES\
						("'+ request.query.id +'","'+ request.query.pw +'","'+request.query.name+'")\
					';
		connection.query(likeSql, function (error, rows, fields) {
				if (error){
					response.sendStatus(400);
					return;
				}
		 		jsondate= '{"title":"not","num":"not"}'; 
		 		response.send(jsondate);
		 		response.end();
		 });
});



//책 리스트
app.get('/booklist', function(request, response){
	 //전체 책의 정보
	var listSql = `	SELECT	num ,
							title ,
							(
								SELECT count(*)
								FROM wb_book_like innerBook
								WHERE outBook.num=innerBook.num_bk
							) AS likenum
					FROM wb_book outBook 
					ORDER BY likenum desc`;
	var jsondate;
	connection.query(listSql, function (error, rows, fields) {
		if (error){
			response.sendStatus(400);
			return;
		}
	 	if(rows.length == 0){
	 		jsondate= '{"num":"not","title":"not","likenum":"not"}';
	 		response.send(jsondate);
	 		response.end();
	 	}else{
	 		
	 		jsondate="";
	 		 for(var i=0;i<rows.length;i++){
	 		 	if(i==0){
	 		 		jsondate= jsondate + '[{"num":"' + rows[i].num;
		 	 		jsondate= jsondate + '","title":"' + rows[i].title + '","likenum":"'+rows[i].likenum+'"}';
	 		 	}else{
	 		 		jsondate= jsondate + ',{"num":"' + rows[i].num;
		 	 		jsondate= jsondate + '","title":"' + rows[i].title + '","likenum":"'+rows[i].likenum+'"}';
	 		 	}
	 		 }
	 		 jsondate=jsondate+"]"
	 		response.send(jsondate);
	 		response.end();
	 	}	
	});
});



//ISBN체크
app.get('/checkisbn', function(request, response){
		var likeSql = ' \
						SELECT isbn	\
						FROM wb_book\
						WHERE isbn= ' +request.query.isbn;

		connection.query(likeSql, function (error, rows, fields) {
				if (error){
					response.sendStatus(400);
					return;
				}
			 	if(rows.length == 0){
			 		jsondate= '{"checkisbn":"good","title":"not","likenum":"not"}';
			 		response.send(jsondate);
			 		response.end();
			 	}else{
			 		jsondate= '{"checkisbn":"not","title":"not","likenum":"not"}';
			 		response.send(jsondate);
			 		response.end();
			 	}	
		 });
});




// //책등록
// app.get('/regibook', function(request, response){
// 		var bookSql = 'INSERT INTO wb_book	(title,isbn) VALUES ("'+ request.query.title +'","'+ request.query.isbn +'");'
// 		response.send(bookSql);

// 		connection.query(bookSql, function (error, rows, fields) {
// 				if (error){
// 					response.sendStatus(error);
// 					return;
// 				}
// 		 		jsondate= '{"checkdata":"good","num":"not"}'; 
// 		 		response.send(jsondate);
// 		 		response.end();
// 		 });
// });


//회원등록
app.get('/regibook', function(request, response){
		var likeSql = ' \
						INSERT INTO wb_book	\
						(title,isbn)\
						VALUES\
						("'+ request.query.title +'","'+ request.query.isbn +'")\
					';
		connection.query(likeSql, function (error, rows, fields) {
				if (error){
					response.sendStatus(400);
					return;
				}
		 		jsondate= '{"title":"not","num":"not"}'; 
		 		response.send(jsondate);
		 		response.end();
		 });
});







//책뷰 내용
app.get('/book', function(request, response){
	var	bookSql = ' SELECT 	title,\
							isbn,\
							imageUrl,\
							(\
							SELECT count(*)\
							FROM wb_book_like\
							WHERE num_bk='+request.query.num_bk+'\
							) AS likenum\
					FROM wb_book\
					WHERE num='+request.query.num_bk+'\
				';
	var jsondate;
	connection.query(bookSql, function (error, rows, fields) {
		if (error){
			response.sendStatus(400);
			return;
		}
 		jsondate= '{"title":"' + rows[0].title;
 		jsondate= jsondate + '","isbn":"' + rows[0].isbn + '","imageUrl":"' + rows[0].imageUrl + '","';
 		jsondate= jsondate + 'likenum":"' + rows[0].likenum + '"}'; 
 		response.send(jsondate);
 		response.end();
	});
});




 
//책에 관한 독서기록 리스트
app.get('/reviewlist', function(request, response){
	var listSql = 'SELECT num,title,num_pf_s\
					FROM wb_book_review WHERE num_bk_s=' + request.query.num_bk;
//	var jsondate;
	connection.query(listSql, function (error, rows, fields) {
		if (error){
			response.sendStatus(400);
			return;
		}
	 	if(rows.length == 0){
	 		jsondate= '{"lengthcheck":"not","title":"not","likenum":"not"}';
	 		response.send(jsondate);
	 		response.end();
	 	}else{
	 		
	 		jsondate="";
	 		 for(var i=0;i<rows.length;i++){
	 		 	if(i==0){
	 		 		jsondate= jsondate + '[{"num":"' + rows[i].num;
	 		 		jsondate= jsondate + '","num_pf_s":"' + rows[i].num_pf_s
		 	 		jsondate= jsondate + '","title":"' + rows[i].title +'"}';
	 		 	}else{
	 		 		jsondate= jsondate + ',{"num":"' + rows[i].num;
	 		 		jsondate= jsondate + '","num_pf_s":"' + rows[i].num_pf_s
		 	 		jsondate= jsondate + '","title":"' + rows[i].title +'"}';
	 		 	}
	 		 }
	 		 jsondate=jsondate+"]"
	 		response.send(jsondate);
	 		response.end();
	 	}	
	});
});



//책에 관한 독서기록 등록
app.get('/reviewsave', function(request, response){
		var bookSql = ' \
						INSERT INTO wb_book_review	\
						(num_pf_s,num_bk_s,title,content)\
						VALUES\
						("'+ request.query.num_pf_s +'","'+ request.query.num_bk_s +'","'+ request.query.texttitle +'","' + request.query.textcontent + '");'
		//response.send(bookSql);

		connection.query(bookSql, function (error, rows, fields) {
				if (error){
					response.sendStatus(error);
					return;
				}
		 		jsondate= '{"checkdata":"good","num":"not"}'; 
		 		response.send(jsondate);
		 		response.end();
		 });
});





//책 좋아요
app.get('/likebk', function(request, response){
		var likeSql = ' \
						INSERT INTO wb_book_like	\
						(num_pf,num_bk)\
						VALUES\
						("'+ request.query.num_pf +'","'+ request.query.num_bk +'")\
					';
		// response.send(likeSql);
		connection.query(likeSql, function (error, rows, fields) {
				if (error){
					response.sendStatus(400);
					return;
				}
		 		jsondate= '{"title":"not","num":"not"}'; 
		 		response.send(jsondate);
		 		response.end();
		 });
});





//독서 기록에 대한 자세한 정보
app.get('/reviewview', function(request, response){
	var listSql = 'SELECT title,content,name\
					FROM wb_book_review br,wb_profile pf WHERE br.num=' + request.query.num_review + "&& pf.num="+request.query.num_pf_s;
	var jsondate;
	connection.query(listSql, function (error, rows, fields) {
		if (error){
			response.sendStatus(400);
			return;
		}
 		jsondate= '{"title":"' + rows[0].title;
 		jsondate= jsondate + '","content":"' + rows[0].content +'", "name":"' + rows[0].name + '"}';
 		response.send(jsondate);
 		response.end();
	 	});
});




//자기자신이 쓴 글 목록
app.get('/myreviewlist', function(request, response){
	 //전체 책의 정보
	var listSql = '	SELECT	num ,title,num_pf_s FROM wb_book_review WHERE num_pf_s='+ request.query.loginnum +' ORDER BY num_bk_s';
	var jsondate;
	connection.query(listSql, function (error, rows, fields) {
		if (error){
			response.sendStatus(400);
			return;
		}
	 	if(rows.length == 0){
	 		jsondate= '{"num":"not","title":"not","likenum":"not"}';
	 		response.send(jsondate);
	 		response.end();
	 	}else{
	 		
	 		jsondate="";
	 		 for(var i=0;i<rows.length;i++){
	 		 	if(i==0){
	 		 		jsondate= jsondate + '[{"num":"' + rows[i].num;
	 		 		jsondate= jsondate + '","num_pf_s":"' + rows[i].num_pf_s
		 	 		jsondate= jsondate + '","title":"' + rows[i].title +'"}';
	 		 	}else{
	 		 		jsondate= jsondate + ',{"num":"' + rows[i].num;
	 		 		jsondate= jsondate + '","num_pf_s":"' + rows[i].num_pf_s
		 	 		jsondate= jsondate + '","title":"' + rows[i].title +'"}';
	 		 	}
	 		 }
	 		 jsondate=jsondate+"]"
	 		response.send(jsondate);
	 		response.end();
	 	}	
	});
});




//자기글 삭제
app.get('/deletereview', function(request, response){
		var bookSql = ' \
						DELETE FROM wb_book_review	\
						WHERE\
						num =' + request.query.num_bk ;
		connection.query(bookSql, function (error, rows, fields) {
				if (error){
					response.sendStatus(400);
					return;
				}
		 		jsondate= '{"checkdata":"good","num":"not"}'; 
		 		response.send(jsondate);
		 		response.end();
		 });
});


app.listen(5029);