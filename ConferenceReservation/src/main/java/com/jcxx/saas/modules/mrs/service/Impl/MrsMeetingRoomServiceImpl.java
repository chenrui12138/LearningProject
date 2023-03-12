package com.jcxx.saas.modules.mrs.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jcxx.saas.modules.mrs.dao.MrsMeetingDao;
import com.jcxx.saas.modules.mrs.dao.MrsMeetingRoomDao;
import com.jcxx.saas.modules.mrs.dao.MrsTimeConfigDao;
import com.jcxx.saas.modules.mrs.entity.MrsMeetingEntity;
import com.jcxx.saas.modules.mrs.entity.MrsMeetingRoomEntity;
import com.jcxx.saas.modules.mrs.entity.MyMrsMeetingRoomEntity;
import com.jcxx.saas.modules.mrs.service.MrsMeetingRoomService;
import com.jcxx.saas.modules.mrs.service.MrsMeetingService;
import com.jcxx.saas.modules.mrs.utils.DateTime;
import com.jcxx.saas.modules.mrs.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wll
 * @create 2021-07-12-22:25
 */
@Service("MrsMeetingRoomService")
public class MrsMeetingRoomServiceImpl extends ServiceImpl<MrsMeetingRoomDao, MrsMeetingRoomEntity> implements MrsMeetingRoomService {
    /**
     * cr
     */
    @Autowired
    private MrsMeetingRoomDao mrsMeetingRoomDao;
    @Autowired
    private MrsMeetingDao mrsMeetingDao;
    @Autowired
    private MrsMeetingService meetingService;
    @Autowired
    private MrsTimeConfigDao mrsTimeConfigDao;

    /**
     * cr
     * 查询 某天（date） 某个会议室的预约情况
     */
    @Override
    public List<MyMrsMeetingRoomEntity> selectMeetingRoom(String date, Integer meetingRoomId) {

        List<MyMrsMeetingRoomEntity> myMrsMeetingRoomEntities = mrsMeetingRoomDao.selectMeetingRoom(date, meetingRoomId);

        return myMrsMeetingRoomEntities;
    }

    /**
     * cr
     * 查询 某天（date） 某个会议室的预约的时间段
     */
    @Override
    public List<MyMrsMeetingRoomEntity> selectNoMeetingRoom(String date, Integer meetingRoomId) {
        List<MyMrsMeetingRoomEntity> myMrsMeetingRoomEntities = mrsMeetingRoomDao.selectNoMeetingRoom(date, meetingRoomId);
        return myMrsMeetingRoomEntities;
    }

    /**
     * cr
     * 查询某天所有会议室的预约情况
     *
     * @param date
     * @return
     */
    @Override
    public ArrayList<Object> selectAllMeetingRoomAllMeeting(String date) {
        List<Map<String, Object>> listMapMeetingRoom = mrsMeetingRoomDao.selectAllMeetingRoom();

        ArrayList<Object> list = new ArrayList<>();

        //查询会议室基本信息
        if (listMapMeetingRoom != null) {
            for (int j = 0; j < listMapMeetingRoom.size(); j++) {
                HashMap<Object, Object> map = new HashMap<>();
                Integer id = (Integer) listMapMeetingRoom.get(j).get("id");

                //会议室的相关信息
                map.put("seat", listMapMeetingRoom.get(j).get("seat"));
                map.put("equipment", listMapMeetingRoom.get(j).get("equipment"));
                map.put("address", listMapMeetingRoom.get(j).get("address"));
                map.put("name", listMapMeetingRoom.get(j).get("name"));
                map.put("meetingRoomId", id);
                map.put("date", date);


                //查询出该会议配置的预约时间
                List<Map<String, Object>> listMapTimeConfig = mrsTimeConfigDao.selectByMeetingRoomId(id);
                //查询已预约的会议
                List<Map<String, Object>> listMapMeeting = mrsMeetingDao.selectByMeetingRoomId(id, date);
                String[] s = new String[]{"", "Two", "Three", "Four"};

                //去除已预约的时间，得出可预约的时间
                for (int i = 0; i < listMapMeeting.size(); i++) {
                    for (int x = 0; x < listMapTimeConfig.size(); x++) {
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
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

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
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

                for (int i = 0; i < s.length; i++) {
                    map.put("motif" + s[i], "可预约");
                }
                //插入已经排序的时间
                for (int i = 0; i < objects.size(); i++) {
                    if (DateTime.timeState(date,(String) objects.get(i).get("startTime")+":00",(String) objects.get(i).get("endTime")+":00")==0){
                        map.put("motif" + s[i], "不可预约");
                    }
                    map.put("startTime" + s[i], DateTime.TimeFormat((String) objects.get(i).get("startTime")));
                    map.put("endTime" + s[i], DateTime.TimeFormat((String) objects.get(i).get("endTime")));
                }

                for (int i = 0; i < listMapMeeting.size(); i++) {
                    if (Objects.equals(map.get("startTime"), DateTime.TimeFormat(simpleDateFormat.format(listMapMeeting.get(i).get("start_time"))))) {
                        map.put("motif", listMapMeeting.get(i).get("motif"));
                    }
                    if (Objects.equals(map.get("startTimeTwo"), DateTime.TimeFormat(simpleDateFormat.format(listMapMeeting.get(i).get("start_time"))))) {
                        map.put("motifTwo", listMapMeeting.get(i).get("motif"));
                    }
                    if (Objects.equals(map.get("startTimeThree"), DateTime.TimeFormat(simpleDateFormat.format(listMapMeeting.get(i).get("start_time"))))) {
                        map.put("motifThree", listMapMeeting.get(i).get("motif"));
                    }
                    if (Objects.equals(map.get("startTimeFour"), DateTime.TimeFormat(simpleDateFormat.format(listMapMeeting.get(i).get("start_time"))))) {
                        map.put("motifFour", listMapMeeting.get(i).get("motif"));
                    }
                }

                //判断会议室的预约状态  0：空闲     1 ：可预约   2：已满
                map.put("meetingRoomState", 2);
                for (int i = 0; i < objects.size(); i++) {
                    if (map.get("motif" + s[i]).equals("可预约")) {
                        if (listMapMeeting.size() == 0) {
                            map.put("meetingRoomState", 0);
                        } else {
                            map.put("meetingRoomState", 1);
                        }
                    }
                }
                list.add(map);
            }
        }
        return list;
    }


    @Override
    public List<MrsMeetingRoomEntity> selectAll() {
        List<MrsMeetingRoomEntity> mrsMeetingRoomEntities = baseMapper.selectAll();
        return mrsMeetingRoomEntities;
    }

    @Override
    public List<MrsMeetingRoomEntity> selectByNameState(String name, Integer state) {
        List<MrsMeetingRoomEntity> mrsMeetingRooms = baseMapper.selectList(new QueryWrapper<MrsMeetingRoomEntity>()
                .like("name", name)
                .eq("state", state)
                .eq("status", 1)
                .orderBy(false, true, "state", "id")
        );
        return mrsMeetingRooms;
    }

    @Override
    public MrsMeetingRoomEntity selectById(Integer id) {
        MrsMeetingRoomEntity roomEntity = baseMapper.selectOne(new QueryWrapper<MrsMeetingRoomEntity>()
                .eq("id", id)
                .orderByAsc("state"));
        return roomEntity;
    }


    @Override
    public int insertMeetingRoom(MrsMeetingRoomEntity roomEntity) {
        int insert = baseMapper.insert(roomEntity);
        return insert;
    }

    @Override
    public int updateMeetingRoom(MrsMeetingRoomEntity roomEntity) {
        int i = baseMapper.updateById(roomEntity);
        return i;
    }

    @Override
    public int deleteMeetingRoom(Integer id) {
        MrsMeetingRoomEntity roomEntity = new MrsMeetingRoomEntity();
        roomEntity.setId(id);
        roomEntity.setStatus(0);
        int i = baseMapper.updateById(roomEntity);
        List<MrsMeetingEntity> meetingEntities = meetingService.selectByMeetingRoom(id);
        for (MrsMeetingEntity meetingEntity : meetingEntities) {
            String startTime = meetingEntity.getStartTime();
            String endTime = meetingEntity.getEndTime();
            String date = meetingEntity.getDate();
            Integer timeState = DateTime.timeState(date, startTime, endTime);
            if (timeState == 2) {
                meetingService.deleteById(meetingEntity.getId());
            }
        }
        return i;
    }

    @Override
    public MrsMeetingRoomEntity selectByTime(String time) {
        MrsMeetingRoomEntity roomEntity = baseMapper.selectOne(new QueryWrapper<MrsMeetingRoomEntity>()
                .eq("create_time", time)
                .eq("status", 1)
        );
        return roomEntity;
    }

    @Override
    public List<MrsMeetingRoomEntity> selectByState(Integer state) {
        List<MrsMeetingRoomEntity> state1 = baseMapper.selectList(new QueryWrapper<MrsMeetingRoomEntity>()
                .eq("state", state)
                .eq("status", 1));
        return state1;
    }

    @Override
    public List<MrsMeetingRoomEntity> selectByName(String name) {
        List<MrsMeetingRoomEntity> name1 = baseMapper.selectList(new QueryWrapper<MrsMeetingRoomEntity>()
                .like("name", name)
                .eq("status", 1)
                .orderByAsc("state"));
        return name1;
    }

}
