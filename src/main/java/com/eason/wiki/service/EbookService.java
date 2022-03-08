package com.eason.wiki.service;


import com.eason.wiki.domain.Ebook;
import com.eason.wiki.domain.EbookExample;
import com.eason.wiki.mapper.EbookMapper;
import com.eason.wiki.req.EbookReq;
import com.eason.wiki.resp.EbookResp;
import com.eason.wiki.util.CopyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);


    @Resource
    private EbookMapper ebookMapper;


    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName()))
            criteria.andNameLike("%"+req.getName()+"%");
        PageHelper.startPage(1,3);
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("Total length: {}",pageInfo.getTotal());
        LOG.info("Total pages: {}",pageInfo.getPages());

        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);
        return  list;
    }
}
