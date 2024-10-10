#!/bin/bash

# 获取当前活动窗口信息
current_focus=$(adb -s TS44231W40116 shell dumpsys window | grep mCurrentFocus)

# 检查是否是目标窗口
while [ true ]; do
  sleep 10
adb -s TS44231W40116 shell input tap 990 304
  sleep 4
  adb -s TS44231W40116 shell input tap 991 52
  sleep 1
  adb -s TS44231W40116 shell input tap 1020 117
  sleep 1
  adb -s TS44231W40116 shell input tap 1057 185
  sleep 1
  adb -s TS44231W40116 shell input tap 1010 259

  sleep 2
  adb -s TS44231W40116 shell input tap 1020 337
  sleep 1
  adb -s TS44231W40116 shell input tap 1026 397
  sleep 1
  adb -s TS44231W40116 shell input tap 1035 456
  sleep 1
  adb -s TS44231W40116 shell input tap 1024 541

  sleep 3
  adb -s TS44231W40116 shell input keyevent 3

  sleep 10
  adb -s TS44231W40116 shell input tap 542 154
  sleep 5
  adb -s TS44231W40116 shell input tap 1756 138

  sleep 600
  adb -s TS44231W40116 shell input tap 1756 138
  sleep 10
  adb -s TS44231W40116 shell input keyevent 3

  sleep 1
      echo "设置应用已关闭"
      adb -s TS44231W40116 shell am force-stop com.sunmidebug.PRJ231013stfzjsfeyrgxdgxypzcghq6mh
      sleep 3
  adb -s TS44231W40116 shell monkey -p com.sunmidebug.PRJ231013stfzjsfeyrgxdgxypzcghq6mh -c android.intent.category.LAUNCHER 1
  echo "执行到这一步"
 sleep 10
adb -s TS44231W40116 shell input tap 990 304
  sleep 2
  adb -s TS44231W40116 shell input tap 1020 337
  sleep 1
  adb -s TS44231W40116 shell input tap 1026 397
  sleep 1
  adb -s TS44231W40116 shell input tap 1035 456
  sleep 1
  adb -s TS44231W40116 shell input tap 1024 541

  sleep 3600
  adb -s TS44231W40116 shell input tap 991 52
  sleep 1
  adb -s TS44231W40116 shell input tap 1020 117
  sleep 1
  adb -s TS44231W40116 shell input tap 1057 185
  sleep 1
  adb -s TS44231W40116 shell input tap 1010 259

  sleep 3600
done