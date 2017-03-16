var page = require(webpage).create(); 网页对象  
var system = require(system);系统对象  
    if(system.args.length==1){  
        console.log(请输入所要请求的url);  
        phantom.exit();  
    }else{  
          
                        page.open(system.args[1],function(){  
                            console.log(arg +  + system.args[1]);  
                            page.render(1 + .png);  
                            phantom.exit();  
                        });  
    }