package com.example.demoapply.entity;

/**
 * 分页类
 * 创建时间：2014年6月28日
 */
public class Page {
	
	private int pageSize; //每页显示记录数
	private int totalPage;		//总页数
	private int totalCount;	//总记录数
	private int pageNo;	//当前页
	private int currentResult;	//当前记录起始索引
	private boolean entityOrField;	//true:需要分页的地方，传入的参数就是Page实体；false:需要分页的地方，传入的参数所代表的实体拥有Page属性
//	private String pageStr;		//最终页面显示的底部翻页导航，详细见：getPageStr();
	private PageData pd = new PageData();
	private boolean openFlag = true;//是否开启分页
	

	
	public Page(){
		try {
			this.pageSize = 10;//显示条数
		} catch (Exception e) {
			this.pageSize = 15;
		}
	}
	
	public int getTotalPage() {
		if(totalCount%pageSize==0)
			totalPage = totalCount/pageSize;
		else
			totalPage = totalCount/pageSize+1;
		return totalPage;
	}
	
	
	

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	

	
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageNo() {
		if(pageNo<=0)
			pageNo = 1;
		if(pageNo>getTotalPage())
			pageNo = getTotalPage();
		return pageNo;
	}
	
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	//拼接分页 页面及JS函数
//	public String getPageStr() {
//		StringBuffer sb = new StringBuffer();
//		if(totalResult>0){
//			sb.append("	<div class=\"dataTables_paginate paging_full_numbers\">\n");
//			if(pageNo==1){
////				sb.append("	<li><a>共<font color=red>"+totalResult+"</font>条</a></li>\n");
////				sb.append("	<li><input type=\"number\" value=\"\" id=\"toGoPage\" style=\"width:50px;text-align:center;float:left\" placeholder=\"页码\"/></li>\n");
////				sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"toTZ();\"  class=\"btn btn-mini btn-success\">跳转</a></li>\n");
//				sb.append("	<span class=\"first paginate_button paginate_button_disabled\"\r\n" + 
//						"						><a >首页</a></span>\n");
//				sb.append("	<span  class=\"previous paginate_button paginate_button_disabled\"><a>上页</a></span>\n");
//			}else{
////				sb.append("	<li><a>共<font color=red>"+totalResult+"</font>条</a></li>\n");
////				sb.append("	<li><input type=\"number\" value=\"\" id=\"toGoPage\" style=\"width:50px;text-align:center;float:left\" placeholder=\"页码\"/></li>\n");
////				sb.append("	<li style=\"cursor:pointer;\"><a onclick=\"toTZ();\"  class=\"btn btn-mini btn-success\">跳转</a></li>\n");
//				sb.append("	<span class=\"first paginate_button paginate_button_disabled\"\r\n" + 
//						"						><a onclick=\"nextPage(1)\">首页</a></span>\n");
//				sb.append("	<span class=\"previous paginate_button paginate_button_disabled\"><a onclick=\"nextPage("+(pageNo-1)+")\">上页</a></span>\n");
//			}
//			int showTag = 5;//分页标签显示数量
//			int startTag = 1;
//			if(pageNo>showTag){
//				startTag = pageNo-1;
//			}
//			int endTag = startTag+showTag-1;
//			for(int i=startTag; i<=totalPage && i<=endTag; i++){
//				if(pageNo==i)
//					sb.append("<span class=\"paginate_active\">"+i+"</span>\n");
//				else
//					sb.append("	<span class=\"paginate_button\"><a onclick=\"nextPage("+i+")\">"+i+"</a></span>\n");
//			}
//			if(pageNo==totalPage){
//				sb.append("	<span class=\"paginate_button\"><a>下页</a></span>\n");
//				sb.append("	<span class=\"paginate_button\"><a>尾页</a></span>\n");
//			}else{
//				sb.append("	<span class=\"paginate_button\"><a onclick=\"nextPage("+(pageNo+1)+")\">下页</a></span>\n");
//				sb.append("	<span class=\"paginate_button\"><a onclick=\"nextPage("+totalPage+")\">尾页</a></span>\n");
//			}
//			sb.append("	<span><a>共"+totalPage+"页</a></span>\n");
//			sb.append("	<label style=\"float:left;\">显示<select title='selectpage' style=\"width:55px;\" onchange=\"changeCount(this.value)\">\n");
//			sb.append("	<option value='"+pageSize+"'>"+pageSize+"</option>\n");
//			sb.append("	<option value='10'>10</option>\n");
//			sb.append("	<option value='20'>20</option>\n");
//			sb.append("	<option value='30'>30</option>\n");
//			sb.append("	<option value='40'>40</option>\n");
//			sb.append("	<option value='50'>50</option>\n");
//			sb.append("	<option value='60'>60</option>\n");
//			sb.append("	<option value='70'>70</option>\n");
//			sb.append("	<option value='80'>80</option>\n");
//			sb.append("	<option value='90'>90</option>\n");
//			sb.append("	<option value='99'>99</option>\n");
//			sb.append("	</select>条\n");
//			sb.append("	</label>\n");
//			
//			sb.append("</div>\n");
//			sb.append("<script type=\"text/javascript\">\n");
//			
//			//换页函数
//			sb.append("function nextPage(page){");
//			sb.append(" ");
//			sb.append("	if(true && document.forms[0]){\n");
//			sb.append("		var url =document.forms[\"Form\"].getAttribute(\"action\") \n");
//			sb.append("		if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"pageNo":"page.pageNo")+"=\";}\n");
//			sb.append("		else{url += \"?"+(entityOrField?"pageNo":"page.pageNo")+"=\";}\n");
//			sb.append("		url = url + page + \"&" +(entityOrField?"pageSize":"page.pageSize")+"="+pageSize+"\";\n");
//			sb.append("		document.forms[0].action = url;\n");
//			sb.append("		document.forms[0].submit();\n");
//			sb.append("	}else{\n");
//			sb.append("		var url = document.location+'';\n");
//			sb.append("		if(url.indexOf('?')>-1){\n");
//			sb.append("			if(url.indexOf('pageNo')>-1){\n");
//			sb.append("				var reg = /pageNo=\\d*/g;\n");
//			sb.append("				url = url.replace(reg,'pageNo=');\n");
//			sb.append("			}else{\n");
//			sb.append("				url += \"&"+(entityOrField?"pageNo":"page.pageNo")+"=\";\n");
//			sb.append("			}\n");
//			sb.append("		}else{url += \"?"+(entityOrField?"pageNo":"page.pageNo")+"=\";}\n");
//			sb.append("		url = url + page + \"&" +(entityOrField?"pageSize":"page.pageSize")+"="+pageSize+"\";\n");
//			sb.append("		document.location = url;\n");
//			sb.append("	}\n");
//			sb.append("}\n");
//			
//			//调整每页显示条数
//			sb.append("function changeCount(value){");
//			sb.append("");
//			sb.append("	if(true && document.forms[0]){\n");
//			sb.append("		var url = document.forms[\"Form\"].getAttribute(\"action\");\n");
//			sb.append("		if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"pageNo":"page.pageNo")+"=\";}\n");
//			sb.append("		else{url += \"?"+(entityOrField?"pageNo":"page.pageNo")+"=\";}\n");
//			sb.append("		url = url + \"1&" +(entityOrField?"pageSize":"page.pageSize")+"=\"+value;\n");
//			sb.append("		document.forms[0].action = url;\n");
//			sb.append("		document.forms[0].submit();\n");
//			sb.append("	}else{\n");
//			sb.append("		var url = document.location+'';\n");
//			sb.append("		if(url.indexOf('?')>-1){\n");
//			sb.append("			if(url.indexOf('pageNo')>-1){\n");
//			sb.append("				var reg = /pageNo=\\d*/g;\n");
//			sb.append("				url = url.replace(reg,'pageNo=');\n");
//			sb.append("			}else{\n");
//			sb.append("				url += \"1&"+(entityOrField?"pageNo":"page.pageNo")+"=\";\n");
//			sb.append("			}\n");
//			sb.append("		}else{url += \"?"+(entityOrField?"pageNo":"page.pageNo")+"=\";}\n");
//			sb.append("		url = url + \"&" +(entityOrField?"pageSize":"page.pageSize")+"=\"+value;\n");
//			sb.append("		document.location = url;\n");
//			sb.append("	}\n");
//			sb.append("}\n");
//			
//			//跳转函数 
//			sb.append("function toTZ(){");
//			sb.append("var toPaggeVlue = document.getElementById(\"toGoPage\").value;");
//			sb.append("if(toPaggeVlue == ''){document.getElementById(\"toGoPage\").value=1;return;}");
//			sb.append("if(isNaN(Number(toPaggeVlue))){document.getElementById(\"toGoPage\").value=1;return;}");
//			sb.append("nextPage(toPaggeVlue);");
//			sb.append("}\n");
//			sb.append("</script>\n");
//		}
//		pageStr = sb.toString();
//		return pageStr;
//	}
	
//	public void setPageStr(String pageStr) {
//		this.pageStr = pageStr;
//	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		
		this.pageSize = pageSize;
	}
	
	public int getCurrentResult() {
		currentResult = (getPageNo()-1)*getPageSize();
		if(currentResult<0)
			currentResult = 0;
		return currentResult;
	}
	
	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}
	
	
	public boolean isOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(boolean openFlag) {
		this.openFlag = openFlag;
	}

	public boolean isEntityOrField() {
		return entityOrField;
	}
	
	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
	}
	
	public PageData getPd() {
		return pd;
	}

	public void setPd(PageData pd) {
		this.pd = pd;
	}
	
}


