<Html>
<head>
<title>freemarkerDemo</title>
<meta charset="utf-8">
</head>
<body>
<#include "head.ftl">
<#--这是一个注释不会输出-->
<!--这是一个注释不会输出-->
${name}你好。 ${message}
</br>
<#assign linkman="李四">
联系人：${linkman}
</br>
<#if success=true>
你已通过实名认证
<#else>
你未通过实名认证
</#if>

</br>
----------商品列表----</br>
<#list goodsList as goods>
${goods_index} 商品名称:${goods.name} 商品列表：${goods.price}</br>
</#list>
一共${goodsList?size}条记录
</br>

<#assign text="{'bank':'工商银行','acount':'25623461423446523'}">
<#assign data=text?eval>
开户行:${data.bank}  账户:${data.acount}

</br>

当前日期:${today?date}</br>
当前时间：${today?time}</br>
当前日期+时间：${today?datetime}</br>
日期格式化:${today?string('yyyy年MM月')}</br>

当前积分：${point?c}</br>

<#if aaa??>
	aaa变量存在 ${aaa}
<#else>
	aaa变量不存在
</#if>
</br>

${bbb!'bbb没有被赋值'}
</br>

<#if point gt 100 >
  黄金会员
</#if>

</body>
</Html>