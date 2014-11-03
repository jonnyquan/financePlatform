package com.sunlights.core.web;

import com.sunlights.common.MsgCode;
import com.sunlights.common.Severity;
import com.sunlights.common.utils.MessageUtil;
import com.sunlights.common.vo.PageVo;
import com.sunlights.common.vo.Message;
import com.sunlights.core.service.ProductService;
import com.sunlights.core.vo.ProductParameter;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import static play.mvc.Results.ok;

/**
 * <p>Project: fsp</p>
 * <p>Title: WebProductService.java</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2014 Sunlights.cc</p>
 * <p>All Rights Reserved.</p>
 *
 * @author <a href="mailto:zhencai.yuan@sunlights.cc">yuanzhencai</a>
 */

public class ProductController extends Controller {
    private Form<ProductParameter> productParameterForm = Form.form(ProductParameter.class);
    private MessageUtil messageUtil = MessageUtil.getInstance();
    private ProductService productService;

    public Result findProductsByType() {
        PageVo pager = new PageVo();
        ProductParameter productParameter = null;
        Http.RequestBody body = request().body();
        if (body.asJson() != null) {
            productParameter = Json.fromJson(body.asJson(), ProductParameter.class);
        }

        if (body.asFormUrlEncoded() != null) {
            productParameter = productParameterForm.bindFromRequest().get();
        }
        System.out.println("[productParameter]" + Json.toJson(productParameter));
        if (productParameter != null) {
            pager.setIndex(productParameter.index);
            pager.setPageSize(productParameter.pageSize);
            if ("0".equals(productParameter.type)) {
                productService.findFunds(pager);
            }
            if ("1".equals(productParameter.type)) {
                productService.findProductRecommends(pager);
            }
            messageUtil.setMessage(new Message(MsgCode.OPERATE_SUCCESS), pager);
        } else {
            messageUtil.setMessage(new Message(Severity.ERROR, MsgCode.SEARCH_FAIL_TYPE_EMPTY), pager);
        }


        return ok(messageUtil.toJson());
    }
}
