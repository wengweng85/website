<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <title>陕西省人力资源市场管理信息系统</title>

    <meta name="keywords" content="xxx">
    <meta name="description" content="xxxx">

    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->

    <link rel="shortcut icon" href="favicon.ico">
    <link href="<c:url value='/resource/hplus/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/resource/hplus/css/font-awesome.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/resource/hplus/css/animate.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/resource/hplus/css/style.min.css'/>" rel="stylesheet">
    <link href="<c:url value='/resource/hplus/css/rc.css'/>" rel="stylesheet">
</head>
<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
    <div id="wrapper">
        <!--左侧导航开始-->
        <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="nav-close"><i class="fa fa-times-circle"></i>
            </div>
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">
                    <div class="dropdown profile-element">
                        <span class="block m-t-xs" style="color: #f9f9f9"><strong class="font-bold"> 陕西省人力资源市场管理信息系统</strong></span>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                        <span class="text-muted text-xs block">${suser.groupname}(${suser.username})<b class="caret"></b></span>
                        </a>
                        <ul class="dropdown-menu animated fadeInRight m-t-xs">
                            <li>
                                <a class="J_menuItem" href="<c:url value='/resources/SXJY_RLZYSC_009_001/tochanagepwd'/>" >修改密码</a>
                            </li>
                        </ul>
                        </div>
                    </li>
                    <shiro:authenticated>
                    <c:forEach items="${permlist}" var="perm" >
                    <li>
                        <a href="#">
                            <i class="fa <c:if test="${ empty perm.iconcss}" >fa-bookmark-o </c:if> <c:if test="${not empty perm.iconcss}" >${perm.iconcss}</c:if> "></i>
                            <span class="nav-label">${perm.name }</span>
                            <c:if test="${not empty perm.child}" >
                                 <span class="fa arrow"></span>
                            </c:if>
                        </a>
                        <c:if test="${not empty perm.child}" >
                        <ul class="nav nav-second-level">
                              <c:forEach items="${perm.child}" var="secondperm" >
						      <li>
							      <c:choose>
	                                 <c:when test="${not empty secondperm.child}">
		                                 <a href="<c:url value='${secondperm.url}'/>" ><i class="fa ${secondperm.iconcss}"></i>${secondperm.name}<span class="fa arrow"></span></a>
		                                 <ul class="nav nav-third-level">
			                                 <c:forEach items="${secondperm.child}" var="thirdperm" >
			                                 <li>
			                                      <a class="J_menuItem" href="<c:url value='${thirdperm.url}'/>" ><i class="fa ${thirdperm.iconcss}"></i>${thirdperm.name}</a>
			                                 </li>
			                                 </c:forEach>
		                                 </ul>
	                                 </c:when>
		                             <c:otherwise>
		                                 <a class="J_menuItem" href="<c:url value='${secondperm.url}'/>" ><i class="fa ${secondperm.iconcss}"></i>${secondperm.name}</a>
		                             </c:otherwise>
	                              </c:choose>
						      </li>
                            </c:forEach>
                         </ul>
                         </c:if>
                     </li>
                    </c:forEach>
                    </shiro:authenticated>
                </ul>
            </div>
        </nav>
        <!--左侧导航结束-->
        <!--右侧部分开始-->
        <div id="page-wrapper" class="gray-bg dashbard-1">
            <div class="row content-tabs">
                <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
                </button>
                <nav class="page-tabs J_menuTabs">
                    <div class="page-tabs-content">
                            <a href="javascript:;" class="active J_menuTab"  data-id="index_v1.html" >首页</a>
                    </div>
                </nav>
                <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
                </button>
                <div class="btn-group roll-nav roll-right">
                    <button class="dropdown J_tabClose" data-toggle="dropdown">关闭操作<span class="caret"></span>
                    </button>
                    <ul role="menu" class="dropdown-menu dropdown-menu-right">
                        <li class="J_tabShowActive"><a>定位当前选项卡</a>
                        </li>
                        <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                        </li>
                        <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                        </li>
                    </ul>
                </div>
                <a href="<c:url value='/loginout'/>" class="roll-nav roll-right J_tabExit"><i class="fa fa fa-sign-out"></i> 退出</a>
            </div>
            <div class="row J_mainContent" id="content-main">
                <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="<c:url value='/index'/>" frameborder="0" data-id="index_v1.html" seamless></iframe>
            </div>
            <div class="footer">
                <div class="pull-right">&copy;2017浙江网新恩普软件有限公司
                </div>
            </div>
        </div>
        <!--右侧部分结束-->
    </div>
    <script src="<c:url value='/resource/hplus/js/jquery.min.js'/>"></script>
    <script src="<c:url value='/resource/hplus/js/bootstrap.min.js'/>"></script>
    <script src="<c:url value='/resource/hplus/js/plugins/metisMenu/jquery.metisMenu.js'/>"></script>
    <script src="<c:url value='/resource/hplus/js/plugins/slimscroll/jquery.slimscroll.min.js'/>"></script>
    <script src="<c:url value='/resource/hplus/js/plugins/layer/layer.min.js'/>"></script>
    <script src="<c:url value='/resource/hplus/js/hplus.min.js'/>"></script>
    <script src="<c:url value='/resource/hplus/js/contabs.min.js'/>"></script>
    <script src="<c:url value='/resource/hplus/js/plugins/pace/pace.min.js'/>"></script>
    <script type="text/javascript">
    $(function(){
    	//$('.navbar-minimalize').click();
    })
    </script>
</body>
</html>