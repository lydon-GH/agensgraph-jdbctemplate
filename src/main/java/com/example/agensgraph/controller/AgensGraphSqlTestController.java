package com.example.agensgraph.controller;

import com.example.agensgraph.common.ResultVO;
import com.example.agensgraph.config.WebAppConfig;
import com.example.agensgraph.dao.*;
import com.example.agensgraph.entity.message.*;
import net.bitnine.agensgraph.graph.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/AgensGraphSqlTestController")
public class AgensGraphSqlTestController {

    @Resource
    private QueryDao queryDao;


    @Autowired
    private transient WebAppConfig webAppConfig ;

    @RequestMapping(value="/test1",method= RequestMethod.GET)
    public	@ResponseBody  ResultVO<List> test1() throws Exception {
        List dataList=new ArrayList();
        try{
            Statement stmt = webAppConfig.getStatementInstance();
            stmt.execute("SET graph_path =  northwind_graph ");
            ResultSet rs = stmt.executeQuery("MATCH (per:person {chinese_name: '张莉敏'})-[:BEYONDS ]-(org:organize{name : '研发中心'}) RETURN per,org");
            while (rs.next()) {
                Vertex person = (Vertex) rs.getObject(1);
                dataList.add(person);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResultVO<List> resultVO=new ResultVO("成功！", dataList, true);
        return resultVO;
    }

    @RequestMapping(value="/threeStep",method= RequestMethod.POST)
    public	@ResponseBody  ResultVO<List> threeStep(String threeStep) throws Exception {

        System.out.println(threeStep+"-----------------");

        List dataList=new ArrayList();
        try{
            Statement stmt = webAppConfig.getStatementInstance();
            stmt.execute("SET graph_path =  northwind_graph ");
            ResultSet rs = stmt.executeQuery("MATCH (per:person {chinese_name: '张莉敏'})-[:BEYONDS ]-(org:organize{name : '研发中心'}) RETURN per,org");
            while (rs.next()) {
                Vertex person = (Vertex) rs.getObject(1);
                dataList.add(person);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResultVO<List> resultVO=new ResultVO("成功！", dataList, true);
        return resultVO;

    }

    @RequestMapping(value="/testJdbcTemplate",method= RequestMethod.GET)
    @ResponseBody
    public	ResultVO<ResultDto>  testJdbcTemplate() throws Exception {
        final RequestDto req = new RequestDto("1234", "12345", "QUERY");
        String sql = " match (n:person {name:'打工人1'}) return n";
        req.setSql(sql);
        req.setOptions("");
        ResultDto resultDto=queryDao.doQuery(req);
        ResultVO<ResultDto> resultVO=new ResultVO("成功！", resultDto, true);
        return resultVO;
    }


    @RequestMapping(value="/testSentinel",method= RequestMethod.GET)
    @ResponseBody
    public	String  testSentinel() {
        return "succes";
    }

}


