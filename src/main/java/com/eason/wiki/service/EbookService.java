package com.eason.wiki.service;


import com.eason.wiki.domain.Ebook;
import com.eason.wiki.domain.EbookExample;
import com.eason.wiki.mapper.EbookMapper;
import com.eason.wiki.req.EbookReq;
import com.eason.wiki.resp.EbookResp;
import com.eason.wiki.util.CopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {



    @Resource
    private EbookMapper ebookMapper;


    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%"+req.getName()+"%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);
        return  list;
    }
}
