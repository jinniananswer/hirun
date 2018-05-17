package ${package};

import com.most.core.pub.data.GenericEntity;

import java.util.Map;

public class ${className} extends GenericEntity{

	public ${className}(){
		super();
	}

	public ${className}(Map<String, String> data){
		super(data);
	}
	
<#list properties as pro>
	public ${pro.proType} get<@upperFC>${pro.proName}</@upperFC>(){
		return this.get("${pro.colName}");
	}

	public void set<@upperFC>${pro.proName}</@upperFC>(${pro.proType} ${pro.proName}){
		this.set("${pro.colName}", ${pro.proName});
	}
	
</#list>
}