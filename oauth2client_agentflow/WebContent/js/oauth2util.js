

(function(window) {
	var nonceChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
	function createNonce(length) {
        var chars = nonceChars;
        var result = "";
        for (var i = 0; i < length; ++i) {
            var rnum = Math.floor(Math.random() * chars.length);
            result += chars.substring(rnum, rnum+1);
        }
        return result;
	}

	function getObjectFromParam(str) {
		var obj = {};
		var params = str.split("&");
		for (var i in params) {
			var temp = params[i].split("=");
			obj[temp[0]] = decodeURIComponent(temp[1]);
		}
		return obj;
	}
	
	window.createNonce = createNonce;
	window.getObjectFromParam = getObjectFromParam;
})(window);

