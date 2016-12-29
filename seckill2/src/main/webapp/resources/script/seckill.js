//存放主要交互逻辑js代码
//javascript模块化
//seckill.detail.init(param)
var seckill = {
	//封装秒杀相关ajax的url
    URL: {

        now: function () {
            return '/seckill/time/now';
        },
        exposer: function(seckillId){
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function(seckillId, md5){
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    
    handleSeckill: function(seckillId, node){
        node.hide()
            .html('<button class="btn bg-primary btn-lg" id="killBtn">start seckill</button>');
        $.post(seckill.URL.exposer(seckillId),{}, function(result){
            console.log(result.success);
            console.log(result.data);
            if(result && result['success']){
                var exposer = result['data'];
                if(exposer['exposed']){
                    //start seckill 开启秒杀
                    //get seckill address 获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killUrl:"+killUrl);
                    //只绑定一次点击事件
                    $('#killBtn').one('click', function(){
                    	//1：先禁用按钮
                        $(this).addClass('disabled');
                        //2：发送秒杀请求，执行秒杀
                        $.post(killUrl, {}, function(result){
                            if(result && result['success']){
                                var killResult = result['data'];
                                var status = killResult['status'];
                                var stateInfo = killResult['statusInfo'];
                                //3：显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                }else{
                    //do not start seckill 未开启秒杀，由于客户机计时的偏差
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新计算计时逻辑
                    seckill.countdown(seckillId, now, start, end);
                }
            }else{
                console.log('result : ' + result);
            }
        });
    },

    //验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {//isNaN(phone)) 非数字判断
            return true;
        } else {
            return false;
        }
    },

    countdown : function(seckillId, nowTime, startTime, endTime){
        var seckillBox = $('#seckill-box');
        if(nowTime > endTime){
        	 //秒杀结束
            seckillBox.html('秒杀结束');
        }else if(nowTime < startTime){
        	//秒杀未开始，计时事件绑定      //+1000 ，防止计时过程的计时偏移；也可以不加
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function(event){
            	// 控制时间的格式
                var format = event.strftime('秒杀倒计时： %DDay %HHour %MMin %SSec');
                seckillBox.html(format);
            }).on('finish.countdown', function(){
            	//时间完成后回调事件
            	//获取秒杀地址，控制实现逻辑，执行秒杀
                seckill.handleSeckill(seckillId, seckillBox);
            });
        }else{
        	//秒杀开始
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },

    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
        	//手机验证和登录，计时交互
        	//规划交互流程
//        	//在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            console.info(killPhone);
            //模拟登录
            if (!seckill.validatePhone(killPhone)) {
            	//绑定phone
                var killPhoneModal = $('#killPhoneModal');
                //控制输出
                killPhoneModal.modal({
                    show: true, //显示弹出层
                    backdrop: 'static', //禁止位置关闭
                    keyboard: false  //关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killphoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                    	//写入cookie
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    } else {
                        $('#killphoneMessage').hide().html('<label class="label label-danger">wrong phone number！</label>').show(300);
                    }
                });
            }
            
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            //已登录
            //计时交互
            $.get(seckill.URL.now(), {}, function (result) {
                if(result && result['success']){
                    var nowTime = result['data'];
                    //时间判断，计时服务
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                }else{
                    console.log('result : ' + result);
                }
            });
        }
    }
};
