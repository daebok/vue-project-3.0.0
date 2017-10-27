package com.rd.ifaes.core.tpp.model.jx;

import com.rd.ifaes.core.tpp.model.jx.json.JxBatchDetailsQuerySubPacksModel;
import com.rd.ifaes.core.tpp.util.JxConfig;

import java.util.List;

public class JxBatchDetailsQueryModel extends JxBaseModel {
    //批次交易日期	A	8	M	YYYYMMDD
    private String batchTxDate;
    //批次号	N	6	M
    private String batchNo;
    //交易种类	A	1	M	0-所有交易(包括1，2，不包括9 )1-成功交易2-失败交易9-合法性校验失败交易
    private String type;
    //页数	N	3	M	查询页数
    private String pageNum;
    //页长	N	2	M	每页笔数
    private String pageSize;

    //批次交易代码	A	50	C	批次号对应的交易代码
    private String batchTxCode;
    //N	5	C
    private String totalItems;
    //结果数组	A		C	JSON数组，内容解释见下文
    private String subPacks;

    private List<JxBatchDetailsQuerySubPacksModel> subPacksModelList;

    /**
     * 请求参数
     */
    private String[] directRequestParamNames = new String[]{"batchTxDate", "batchNo", "type", "pageNum", "pageSize"};
    /**
     * 响应参数
     */
    private String[] directResponseParamNames = new String[]{"batchTxDate", "batchNo", "type", "batchTxCode", "pageNum", "pageSize", "totalItems", "subPacks"};

    public JxBatchDetailsQueryModel(){
        super();
        super.setTxCode(JxConfig.BATCH_DETAILS_QUERY);
    }

    public String getBatchTxDate() {
        return batchTxDate;
    }

    public void setBatchTxDate(String batchTxDate) {
        this.batchTxDate = batchTxDate;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getBatchTxCode() {
        return batchTxCode;
    }

    public void setBatchTxCode(String batchTxCode) {
        this.batchTxCode = batchTxCode;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    public String getSubPacks() {
        return subPacks;
    }

    public void setSubPacks(String subPacks) {
        this.subPacks = subPacks;
    }

    public List<JxBatchDetailsQuerySubPacksModel> getSubPacksModelList() {
        return subPacksModelList;
    }

    public void setSubPacksModelList(List<JxBatchDetailsQuerySubPacksModel> subPacksModelList) {
        this.subPacksModelList = subPacksModelList;
    }

    @Override
    public String[] getDirectRequestParamNames() {
        return directRequestParamNames;
    }

    @Override
    public void setDirectRequestParamNames(String[] directRequestParamNames) {
        this.directRequestParamNames = directRequestParamNames;
    }

    @Override
    public String[] getDirectResponseParamNames() {
        return directResponseParamNames;
    }

    @Override
    public void setDirectResponseParamNames(String[] directResponseParamNames) {
        this.directResponseParamNames = directResponseParamNames;
    }
}
