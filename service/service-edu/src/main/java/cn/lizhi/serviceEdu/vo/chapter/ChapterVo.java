package cn.lizhi.serviceEdu.vo.chapter;

import lombok.Data;

import java.util.List;

@Data
public class ChapterVo {
    /**
     * 章节Vo类
     */

    private String id;
    private String title;
    private List<VideoVo> children;
}
