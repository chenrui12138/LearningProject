package com.jcxx.saas.modules.mrs.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jcxx.saas.modules.mrs.dao.MrsMeetingDao;
import com.jcxx.saas.modules.mrs.dao.MrsTimeConfigDao;
import com.jcxx.saas.modules.mrs.entity.*;
import com.jcxx.saas.modules.mrs.service.MrsMeetingService;
import com.jcxx.saas.modules.mrs.utils.DateTime;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("MrsMeetingService")
public class MrsMeetingServiceImpl extends ServiceImpl<MrsMeetingDao, MrsMeetingEntity> implements MrsMeetingService {

    /**
     * cr
     */
    @Autowired
    private MrsMeetingDao mrsMeetingDao;

    /**
     * cr
     * 查询此用户的所有会议
     */
    @Override
    public List<MyMeetingEntity> selectAllMyMeeting(Integer creatorId) {
        List<MyMeetingEntity> myMeetingEntities = mrsMeetingDao.selectAllMyMeeting(creatorId);
        for (int i = 0; i < myMeetingEntities.size(); i++) {
            String startTime = myMeetingEntities.get(i).getStartTime();
            String endTime = myMeetingEntities.get(i).getEndTime();
            String date = myMeetingEntities.get(i).getDate();

            //判断当前时间的会议状态， 2：未开始 1：会议进行中 0：会议已结束
            if (DateTime.timeState(date, startTime, endTime) == 0) {
                myMeetingEntities.get(i).setState(0);
            }
            if (DateTime.timeState(date, startTime, endTime) == 1) {
                myMeetingEntities.get(i).setState(1);
            }
            if (DateTime.timeState(date, startTime, endTime) == 2) {
                myMeetingEntities.get(i).setState(2);
            }

            myMeetingEntities.get(i).setStartTime(DateTime.TimeFormat(myMeetingEntities.get(i).getStartTime()));
            myMeetingEntities.get(i).setEndTime(DateTime.TimeFormat(myMeetingEntities.get(i).getEndTime()));
        }

        //排序  会议中在第一位，然后是未开始，最后是已结束,同状态下时间小的排前面
        MyMeetingEntity hashMap;
        //按时间进行排序
        for (int i = 0; i < (myMeetingEntities.size() - 1); i++) {
            for (int x = 0; x < (myMeetingEntities.size() - i - 1); x++) {
                if (Long.parseLong(myMeetingEntities.get(x).getDate().replace("-", "") + myMeetingEntities.get(x).getStartTime().replace(":", "")) >
                        Long.parseLong(myMeetingEntities.get(x + 1).getDate().replace("-", "") + myMeetingEntities.get(x + 1).getStartTime().replace(":", ""))) {
                    hashMap = myMeetingEntities.get(x);
                    myMeetingEntities.set(x, myMeetingEntities.get(x + 1));
                    myMeetingEntities.set(x + 1, hashMap);
                }
            }
        }
        ArrayList<MyMeetingEntity> list = new ArrayList<>();
        for (MyMeetingEntity myMeetingEntity : myMeetingEntities) {
            if (myMeetingEntity.getState() == 1) {
                list.add(myMeetingEntity);
            }
        }
        for (MyMeetingEntity myMeetingEntity : myMeetingEntities) {
            if (myMeetingEntity.getState() == 2) {
                list.add(myMeetingEntity);
            }
        }
        for (MyMeetingEntity myMeetingEntity : myMeetingEntities) {
            if (myMeetingEntity.getState() == 0) {
                list.add(myMeetingEntity);
            }
        }
        return list;
    }

    /**
     * cr
     * 添加会议预约
     */
    @Override
    public int addMeeting(MrsMeetingEntity mrsMeetingEntity) {
        return baseMapper.insert(mrsMeetingEntity);
    }

    /**
     * cr
     * 查询某个会议室某一天的预约情况
     */
    @Override
    public List<MyMeetingTimeEntity> selectOneDayMeeting(String date, Integer meetingRoomId) {
        List<MyMeetingTimeEntity> mrsMeetingEntities = mrsMeetingDao.selectOneDayMeeting(date, meetingRoomId);
        return mrsMeetingEntities;
    }

    /**
     * cr
     * 文件上传
     *
     * @param file
     * @return
     */
    @Value("${file.path}")
    private String filePath;

    @Override
    public String excelUpload(MultipartFile file) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

        // 并且在 uploadFile 文件夹中通过日期对上传的文件归类保存
        String format = sdf.format(new Date());
        File folder = new File(filePath + File.separator + format);

        if (!folder.isDirectory() && folder != null) {
            folder.mkdirs();
        }

        // 对上传的文件重命名，避免文件重名
        String newName = UUID.randomUUID().toString() + "#" + file.getOriginalFilename();
        try {
            // 文件保存
            file.transferTo(new File(folder, newName));

            // 返回上传文件的访问路径
            String path = filePath + File.separator + format + newName;

            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败!";
    }


    /**
     * cr
     */
    @Autowired
    private MrsTimeConfigDao mrsTimeConfigDao;

    /**
     * cr
     * 会议室大屏
     * 查询此会议室当天的已经预约的会议和未预约会议的时间
     *
     * @return
     */
    @Override
    public ArrayList<Object> selectMeetingDay(String date, Integer meetingRoomId) {
        ArrayList<Object> list = new ArrayList<>();

        //查询出该会议配置的预约时间
        List<Map<String, Object>> listMapTimeConfig = mrsTimeConfigDao.selectByMeetingRoomId(meetingRoomId);
        //查询已预约的会议
        List<Map<String, Object>> listMapMeeting = mrsMeetingDao.selectByMeetingRoomId(meetingRoomId, date);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
        //去除已预约的时间，得出可预约的时间
        for (int i = 0; i < listMapMeeting.size(); i++) {
            for (int x = 0; x < listMapTimeConfig.size(); x++) {

                //已经预约会议的开始结束时间
                String startTime = simpleDateFormat1.format(listMapMeeting.get(i).get("start_time"));
                String endTime = simpleDateFormat1.format(listMapMeeting.get(i).get("end_time"));

                String start_time = simpleDateFormat1.format(listMapTimeConfig.get(x).get("start_time"));
                String end_time = simpleDateFormat1.format(listMapTimeConfig.get(x).get("end_time"));

                if (DateTime.DateTimeState(endTime, start_time, end_time) || DateTime.DateTimeState(startTime, start_time, end_time)) {
                    listMapTimeConfig.remove(x);
                }
            }
        }

        //设置一天预约的会议不能超过四场
        for (int i = 1; i < listMapTimeConfig.size() + 1; i++) {
            if (listMapTimeConfig.size() + listMapMeeting.size() > 4) {
                listMapTimeConfig.remove(listMapTimeConfig.size() - i);
            }
        }

        //取出所有时间
        ArrayList<HashMap> objects = new ArrayList<HashMap>();
        for (Map<String, Object> stringObjectMap : listMapMeeting) {
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("startTime", simpleDateFormat.format(stringObjectMap.get("start_time")));
            stringStringHashMap.put("endTime", simpleDateFormat.format(stringObjectMap.get("end_time")));
            objects.add(stringStringHashMap);
        }
        for (Map<String, Object> stringObjectMap : listMapTimeConfig) {
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            stringStringHashMap.put("startTime", simpleDateFormat.format(stringObjectMap.get("start_time")));
            stringStringHashMap.put("endTime", simpleDateFormat.format(stringObjectMap.get("end_time")));
            objects.add(stringStringHashMap);
        }

        HashMap<String, String> hashMap;
        //按时间进行排序
        for (int i = 0; i < (objects.size() - 1); i++) {
            for (int x = 0; x < (objects.size() - i - 1); x++) {
                if (Integer.parseInt(((String) objects.get(x).get("startTime")).replace(":", "")) >
                        Integer.parseInt(((String) objects.get(x + 1).get("startTime")).replace(":", ""))) {
                    hashMap = objects.get(x);
                    objects.set(x, objects.get(x + 1));
                    objects.set(x + 1, hashMap);
                }
            }
        }

        //插入已经排序的时间
        for (int i = 0; i < objects.size(); i++) {
            HashMap<Object, Object> map = new HashMap<>();
            map.put("startTime", DateTime.TimeFormat((String) objects.get(i).get("startTime")));
            map.put("endTime", DateTime.TimeFormat((String) objects.get(i).get("endTime")));
            map.put("meetingRoomId", meetingRoomId);

            List<Map<String, Object>> listMapTimeConfig1 = mrsTimeConfigDao.selectByMeetingRoomId(meetingRoomId);
            map.put("name", listMapTimeConfig1.get(0).get("name"));

            map.put("meetingScreenState", 0);

            //当前时间大于会议结束时间则 meetingScreenState=2，已过会议时间状态
            if (Integer.valueOf(DateTime.nowTime().replace(":", ""))
                    > Integer.valueOf(((String) map.get("endTime")).replace(":", "") + "00")) {
                map.put("meetingScreenState", 2);
            }

            for (int j = 0; j < listMapMeeting.size(); j++) {
                //已经预约的会议状态判断
                if (((String) objects.get(i).get("startTime")).replace(":", "")
                        .equals(simpleDateFormat.format(listMapMeeting.get(j).get("start_time")).replace(":", ""))) {
                    if (Integer.valueOf(DateTime.nowTime().replace(":", ""))
                            > Integer.valueOf(((String) map.get("endTime")).replace(":", "") + "00")) {
                        map.put("meetingScreenState", 2);
                        break;
                    } else {
                        map.put("meetingScreenState", 1);
                        break;
                    }
                }
            }
            list.add(map);
        }
        return list;
    }

    @Override
    public int updateByMeetingId(Integer id) {

        MrsMeetingEntity mrsMeetingEntity = new MrsMeetingEntity();
        mrsMeetingEntity.setId(id);
        mrsMeetingEntity.setSign(1);
        int i = baseMapper.updateById(mrsMeetingEntity);

        return i;
    }

    @Override
    public IPage<UserMeetingVO> selectAll(Page<UserMeetingVO> page) {
        List<UserMeetingVO> userMeetingVOS = baseMapper.selectAll(page);
        Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
        return DateTime.nowState(userMeetingVOPage);
    }

    @Override
    public IPage<UserMeetingVO> selectByState(Page<UserMeetingVO> page, Integer state) {
        String nowDate = DateTime.nowDate();
        String nowTime = DateTime.nowTime();
        if (state == 0) {
            List<UserMeetingVO> userMeetingVOS = baseMapper.selectByState0(page, nowDate, nowTime);
            Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
            return DateTime.nowState(userMeetingVOPage);
        }
        if (state == 1) {
            List<UserMeetingVO> userMeetingVOS = baseMapper.selectByState1(page, nowDate, nowTime);
            Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
            return DateTime.nowState(userMeetingVOPage);
        }

        List<UserMeetingVO> userMeetingVOS = baseMapper.selectByState2(page, nowDate, nowTime);
        Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
        return DateTime.nowState(userMeetingVOPage);
    }

    @Override
    public IPage<UserMeetingVO> selectByMotif(Page<UserMeetingVO> page, String motif) {
        List<UserMeetingVO> userMeetingVOS = baseMapper.selectByMotif(page, motif);
        Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
        return DateTime.nowState(userMeetingVOPage);
    }

    @Override
    public IPage<UserMeetingVO> selectByDate(Page<UserMeetingVO> page, String date) {
        List<UserMeetingVO> userMeetingVOS = baseMapper.selectByDate(page, date);
        Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
        return DateTime.nowState(userMeetingVOPage);
    }

    @Override
    public IPage<UserMeetingVO> selectByMotifState(Page<UserMeetingVO> page, String motif, Integer state) {
        String nowDate = DateTime.nowDate();
        String nowTime = DateTime.nowTime();
        if (state == 0) {
            List<UserMeetingVO> userMeetingVOS = baseMapper.selectByMotifState0(page, nowDate, nowTime, motif);
            Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
            return DateTime.nowState(userMeetingVOPage);
        }
        if (state == 1) {
            List<UserMeetingVO> userMeetingVOS = baseMapper.selectByMotifState1(page, nowDate, nowTime, motif);
            Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
            return DateTime.nowState(userMeetingVOPage);
        }
        List<UserMeetingVO> userMeetingVOS = baseMapper.selectByMotifState2(page, nowDate, nowTime, motif);
        Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
        return DateTime.nowState(userMeetingVOPage);
    }

    @Override
    public IPage<UserMeetingVO> selectByDateMotif(Page<UserMeetingVO> page, String date, String motif) {
        List<UserMeetingVO> userMeetingVOS = baseMapper.selectByDateMotif(page, date, motif);
        Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
        return DateTime.nowState(userMeetingVOPage);
    }

    @Override
    public IPage<UserMeetingVO> selectByDateState(Page<UserMeetingVO> page, String date, Integer state) {
        String nowDate = DateTime.nowDate();
        String nowTime = DateTime.nowTime();
        if (state == 0) {
            List<UserMeetingVO> userMeetingVOS = baseMapper.selectByDateState0(page, nowDate, nowTime, date);
            Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
            return DateTime.nowState(userMeetingVOPage);
        }
        if (state == 1) {
            List<UserMeetingVO> userMeetingVOS = baseMapper.selectByDateState1(page, nowDate, nowTime, date);
            Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
            return DateTime.nowState(userMeetingVOPage);
        }
        List<UserMeetingVO> userMeetingVOS = baseMapper.selectByDateState2(page, nowDate, nowTime, date);
        Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
        return DateTime.nowState(userMeetingVOPage);
    }

    @Override
    public IPage<UserMeetingVO> selectByDateMotifState(Page<UserMeetingVO> page, String date, String motif, Integer state) {
        String nowDate = DateTime.nowDate();
        String nowTime = DateTime.nowTime();
        if (state == 0) {
            List<UserMeetingVO> userMeetingVOS = baseMapper.selectByDateMotifState0(page, nowDate, nowTime, date, motif);
            Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
            return DateTime.nowState(userMeetingVOPage);
        }
        if (state == 1) {
            List<UserMeetingVO> userMeetingVOS = baseMapper.selectByDateMotifState1(page, nowDate, nowTime, date, motif);
            Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
            return DateTime.nowState(userMeetingVOPage);
        }
        List<UserMeetingVO> userMeetingVOS = baseMapper.selectByDateMotifState2(page, nowDate, nowTime, date, motif);
        Page<UserMeetingVO> userMeetingVOPage = page.setRecords(userMeetingVOS);
        return DateTime.nowState(userMeetingVOPage);
    }

    @Override
    public UserMeetingVO selectByMeetingId(Integer id) {
        UserMeetingVO userMeetingVO = baseMapper.selectByMeetingId(id);
        userMeetingVO.setStartTime(DateTime.TimeFormat(userMeetingVO.getStartTime()));
        userMeetingVO.setEndTime(DateTime.TimeFormat(userMeetingVO.getEndTime()));
        return userMeetingVO;
    }


    @Override
    public String upload(MultipartFile file) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

        // 并且在 uploadFile 文件夹中通过日期对上传的文件归类保存
        String format = sdf.format(new Date());
        File folder = new File(filePath + File.separator + format);

        if (!folder.isDirectory() || folder != null) {
            folder.mkdirs();
        }

        // 对上传的文件重命名，避免文件重名
        String newName = UUID.randomUUID().toString() + "#" + file.getOriginalFilename();

        try {
            // 文件保存
            file.transferTo(new File(folder, newName));

            // 返回上传文件的访问路径
            String path = filePath + File.separator + format + newName;

            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateMeetingById(MrsMeetingEntity meetingEntity) {
        int i = baseMapper.updateById(meetingEntity);
        return i;
    }

    @Override
    public boolean down(Integer id, HttpServletResponse response) {
        UserMeetingVO userMeetingVO = baseMapper.selectByMeetingId(id);
        try {
            String path = userMeetingVO.getParticipant();
            String[] split = path.split("#", 2);
            FileInputStream fileInputStream = new FileInputStream(new File(path));
            ServletOutputStream outputStream = response.getOutputStream();

            response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(split[1], "UTF-8"));
            IOUtils.copy(fileInputStream, outputStream);
            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(outputStream);
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }

    @Override
    public int deleteById(Integer id) {
        MrsMeetingEntity mrsMeetingEntity = new MrsMeetingEntity();
        mrsMeetingEntity.setId(id);
        mrsMeetingEntity.setStatus(0);
        int i = baseMapper.updateById(mrsMeetingEntity);
        return i;
    }

    @Override
    public List<MrsMeetingEntity> selectByMeetingRoom(Integer id) {
        List<MrsMeetingEntity> meetingEntities = baseMapper.selectList(new QueryWrapper<MrsMeetingEntity>()
                .eq("meeting_room_id", id)
                .eq("status", 1)
        );
        return meetingEntities;
    }

    @Override
    public boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                return file.delete();
            }
        }
        return false;
    }


}



