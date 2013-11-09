function request(url, sendData, callBack){
	$.ajax({
		url:url,
		type:"GET",
		data:sendData,
		success:function(result){
			callBack(result)
		}
	})
}
