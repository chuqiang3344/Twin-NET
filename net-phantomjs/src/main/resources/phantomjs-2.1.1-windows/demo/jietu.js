var page = require(webpage).create(); ��ҳ����  
var system = require(system);ϵͳ����  
    if(system.args.length==1){  
        console.log(��������Ҫ�����url);  
        phantom.exit();  
    }else{  
          
                        page.open(system.args[1],function(){  
                            console.log(arg +  + system.args[1]);  
                            page.render(1 + .png);  
                            phantom.exit();  
                        });  
    }