var formidable = require('formidable');
var savePath = './upload/';
var express = require('express');
//var path = require('path');
var mysql = require('mysql');
var fs = require('fs');
//var upload = multer({ storage: storage })
var connection = mysql.createConnection({
    host	: 'localhost',
    query	: { pool : true },
    user	: 'owen',
    password : 'owen',
    database : 'osam',
	multipleStatements : true //다중 쿼리를 수행가능하게 설정하는 부분
});
var app = express();
connection.connect();






//로그인
app.get('/logincheck', function(request, response){
	var loginSql = 'SELECT num,id FROM wb_profile WHERE id=' + request.query.id + '&& pw=' + request.query.pw;
	var jsondate;
	connection.query(loginSql, function (error, rows) {
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
		var jsondate;
		connection.query(likeSql, function (error, rows) {
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
		var jsondate;
		connection.query(likeSql, function (error) {
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
	var listSql = 'SELECT	num ,\
							title ,\
							(\
								SELECT count(*)\
								FROM wb_book_like innerBook\
								WHERE outBook.num=innerBook.num_bk\
							) AS likenum\
					FROM wb_book outBook \
					ORDER BY likenum desc';
	var jsondate;
    
	connection.query(listSql, function (error, rows) {
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

		connection.query(likeSql, function (error, rows) {
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


//책등록
var isFormData= function(req){
	var type = req.headers['content-type'] || '';
	return 0 == type.indexOf('multipart/form-data');
}

app.post('/regibookimg', function (req, res){
    var form = new formidable.IncomingForm();
	var body = {};
	var finename;
    
	if(!isFormData(req)){
		res.sendStatus(400).end('Bad request');
		console.log('bad');
		return;
	}
	
	form.parse(req);
	form.on('field', function (name, value,err){
		if(err){
			console.log('ond');
		}else{
			body[name]=value;	
		}
		
    });
    form.on('fileBegin', function (name, file,err){
				if(err){
			console.log('on');
		}
        file.path = savePath + new Date().valueOf() + file.name;
		finename=new Date().valueOf() + file.name;
    });
	form.on('end', function(err) {
				if(err){
			console.log('end');
		}
		var sql =	'INSERT INTO wb_book (title, isbn, imageurl)' +
					'VALUES(?,?,?)';
		var args = [body.title, body.isbn, finename];
		connection.query(sql, args, function(err) {
		 if (err) {
			 res.sendStatus(500);
			 console.log(args);
			 return;
		 }
			res.sendStatus(200);
		 });
	}); 
});





app.get('/image/:filename',function (req, res){
	var path = savePath + req.params.filename;
	fs.exists(path, function(exists){
		if(exists){
			var stream = fs.createReadStream(savePath + req.params.filename);
			stream.pipe(res);
			stream.on('close',function(){
				res.end;		  
			});
		}else{
			res.sendStatus(204)
		}	
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
	connection.query(bookSql, function (error, rows) {
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
	var jsondate;
	connection.query(listSql, function (error, rows) {
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

		connection.query(bookSql, function (error) {
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
		connection.query(likeSql, function (error) {
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
	connection.query(listSql, function (error, rows) {
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
	connection.query(listSql, function (error, rows) {
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
		var bookSql = ' DELETE FROM wb_book_review\
						WHERE num =' + request.query.num_bk ;
		connection.query(bookSql, function (error) {
				if (error){
					response.sendStatus(400);
					return;
				}
		 		jsondate= '{"checkdata":"good","num":"not"}'; 
		 		response.send(jsondate);
		 		response.end();
		 });
});





//추천 알고리즘
app.get('/recommendbook', function(request, response){
		var num_pf = request.query.num_pf;
		var userBookSql =	'	SELECT	num_bk\
								FROM	wb_book_like\
								GROUP BY	num_pf,num_bk\
								HAVING	num_pf = ' + num_pf + '; \
								\
								SELECT num_bk,num_pf,title\
								FROM\
										(\
										SELECT	num_bk,\
												num_pf\
										FROM	wb_book_like\
										GROUP BY	num_pf,num_bk\
										HAVING	num_pf <> ' + num_pf + '\
										ORDER BY	num_pf\
										) likeb, wb_book\
								WHERE likeb.num_bk=wb_book.num\
								ORDER BY num_pf;\
								\
								SELECT COUNT(*) number\
								FROM\
									(\
									SELECT	num_pf\
									FROM	wb_book_like\
									GROUP BY	num_pf\
									HAVING	num_pf <> ' + num_pf + '\
									) COUNTTABLE';
	
		//유저가 좋아한 likelist
		var userLikeBookArray;
		//추천할 책 list(Last)
		var recommenBookLastArray;
		//추천할 책 list(temp)
		var recommenBookArray;
		//추천할 user
		var recommenUserArray;
		//추천할 user
		var recommenUser;
		//추천할 책 전체list
		var candidateArray;
		//같은책의 갯수
		var sameNumber; 
		var same=0;
		//분할 값 갯수
		var separ;
		//for문을 위한 분할
		var forstart;
		var forend;
		var num;
		//임시 저장소
		var tempArray;
		//데이터 유지를 위한 변수
		var n;
		
	
		connection.query(userBookSql, function(err,rows){								  
			if(err){
				response.sendStatus(400);
				return;
				
			}else if(rows.length == 0){
				response.send('{"title":"null"}');
				response.end;
				
			}else if(rows.length != 0){
				userLikeBookArray = rows[0];	//(num_bk)
				candidateArray = rows[1]; 		//(num_bk,num_pf)
				num = rows[2][0].number;
				separ  = new Array(num);
				recommenUserArray = new Array(num);
				//==================================================================분할 위치 설정(m.i)
				separ[0] = 0;
				recommenUserArray[0] = candidateArray[0].num_pf;
				n=0;
				
				for(var m=1;m<num;m++){
					n=n+1;
					for(var i=n;i<candidateArray.length;i++){
						if(candidateArray[i-1].num_pf != candidateArray[i].num_pf){
							recommenUserArray[m] = candidateArray[i].num_pf;
							separ[m]=i;
							break;
						}
						n=n+1;
					}
				}
				//==================================================================
				sameNumber= new Array(num);
				
				//==================================================================비교 시작(z,k,l)	
				for(var z=0;z<userLikeBookArray.length;z++){
					
						forstart = separ[z];
						if(z==separ.length-1){
							forend = candidateArray.length-1;
						}else{
							forend = separ[z+1]-1;
						}		

						for(var l=forstart ; l<forend+1;l++){
							for(var k=0;k<userLikeBookArray.length;k++){
								if(userLikeBookArray[k].num_bk == candidateArray[l].num_bk){
									same++;	
								}
							}
						}
						sameNumber[z]=same;
						same=0;	
				}
				//==================================================================
				//==================================================================정렬 후 추천 목록 표시(p,o)
				tempArray = sameNumber.slice();
				//내림차순 정렬
				tempArray.sort(function(a, b) {
					return b - a;
				});
				
				recommenBookArray=new Array(tempArray[0]);
				
				for(var p=0;p<sameNumber.length;p++){
					if(tempArray[0]==sameNumber[p]){
						recommenUser = recommenUserArray[p];
						break;
					}
				}
				
				n=0;				
				for(var o=0;o<candidateArray.length;o++){
					if(recommenUser==candidateArray[o].num_pf){
						recommenBookArray[n]=candidateArray[o].num_bk;
						n++;
					}
				}
				
				recommenBookLastArray=new Array();
				
				for(var y=0;y<userLikeBookArray.length;y++){
					n=0;
					for(var u=0;u<recommenBookArray.length;u++){
						if(userLikeBookArray[y].num_bk == recommenBookArray[u]){
							for(var r=0;r<recommenBookArray.length;r++){
								if(r!=u){
									recommenBookLastArray[n] = recommenBookArray[r];
									n++;
								}
							}
							recommenBookArray = recommenBookLastArray.slice();
							recommenBookLastArray=new Array();
							break;
						}		
					}
				}
				//==================================================================
				var senddata='[';
				
				for(var b=0;b<recommenBookArray.length;b++){
					for(var c=0;c<candidateArray.length;c++){
						if(recommenBookArray[b]==candidateArray[c].num_bk){
							if(b==0){
								senddata = senddata + '{"title":"' + candidateArray[c].title	+'",';
								senddata = senddata + '"num":"' + candidateArray[c].num_bk	+'"}';	
							}else{
								senddata = senddata + ',{"title":"' + candidateArray[c].title	+'",';
								senddata = senddata + '"num":"' + candidateArray[c].num_bk	+'"}';	
							}
							break;
						}
					}
				}
				senddata=senddata + ']';
				response.send(senddata);
				response.end;
			}
			
		});
});





app.listen(5029);
