/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
$(document).ready(function() {

    $(".click-title").mouseenter( function(    e){
        e.preventDefault();
        this.style.cursor="pointer";
    });
    $(".click-title").mousedown( function(event){
        event.preventDefault();
    });

    // Ugly code while this script is shared among several pages
    try{
        refreshHitsPerSecond(true);
    } catch(e){}
    try{
        refreshResponseTimeOverTime(true);
    } catch(e){}
    try{
        refreshResponseTimePercentiles();
    } catch(e){}
});


var responseTimePercentilesInfos = {
        data: {"result": {"minY": 541.0, "minX": 0.0, "maxY": 1513.0, "series": [{"data": [[0.0, 541.0], [0.1, 541.0], [0.2, 541.0], [0.3, 541.0], [0.4, 541.0], [0.5, 541.0], [0.6, 541.0], [0.7, 541.0], [0.8, 541.0], [0.9, 541.0], [1.0, 631.0], [1.1, 631.0], [1.2, 631.0], [1.3, 631.0], [1.4, 631.0], [1.5, 631.0], [1.6, 631.0], [1.7, 631.0], [1.8, 631.0], [1.9, 631.0], [2.0, 688.0], [2.1, 688.0], [2.2, 688.0], [2.3, 688.0], [2.4, 688.0], [2.5, 688.0], [2.6, 688.0], [2.7, 688.0], [2.8, 688.0], [2.9, 688.0], [3.0, 716.0], [3.1, 716.0], [3.2, 716.0], [3.3, 716.0], [3.4, 716.0], [3.5, 716.0], [3.6, 716.0], [3.7, 716.0], [3.8, 716.0], [3.9, 716.0], [4.0, 773.0], [4.1, 773.0], [4.2, 773.0], [4.3, 773.0], [4.4, 773.0], [4.5, 773.0], [4.6, 773.0], [4.7, 773.0], [4.8, 773.0], [4.9, 773.0], [5.0, 800.0], [5.1, 800.0], [5.2, 800.0], [5.3, 800.0], [5.4, 800.0], [5.5, 800.0], [5.6, 800.0], [5.7, 800.0], [5.8, 800.0], [5.9, 800.0], [6.0, 818.0], [6.1, 818.0], [6.2, 818.0], [6.3, 818.0], [6.4, 818.0], [6.5, 818.0], [6.6, 818.0], [6.7, 818.0], [6.8, 818.0], [6.9, 818.0], [7.0, 826.0], [7.1, 826.0], [7.2, 826.0], [7.3, 826.0], [7.4, 826.0], [7.5, 826.0], [7.6, 826.0], [7.7, 826.0], [7.8, 826.0], [7.9, 826.0], [8.0, 832.0], [8.1, 832.0], [8.2, 832.0], [8.3, 832.0], [8.4, 832.0], [8.5, 832.0], [8.6, 832.0], [8.7, 832.0], [8.8, 832.0], [8.9, 832.0], [9.0, 917.0], [9.1, 917.0], [9.2, 917.0], [9.3, 917.0], [9.4, 917.0], [9.5, 917.0], [9.6, 917.0], [9.7, 917.0], [9.8, 917.0], [9.9, 917.0], [10.0, 917.0], [10.1, 917.0], [10.2, 917.0], [10.3, 917.0], [10.4, 917.0], [10.5, 917.0], [10.6, 917.0], [10.7, 917.0], [10.8, 917.0], [10.9, 917.0], [11.0, 918.0], [11.1, 918.0], [11.2, 918.0], [11.3, 918.0], [11.4, 918.0], [11.5, 918.0], [11.6, 918.0], [11.7, 918.0], [11.8, 918.0], [11.9, 918.0], [12.0, 948.0], [12.1, 948.0], [12.2, 948.0], [12.3, 948.0], [12.4, 948.0], [12.5, 948.0], [12.6, 948.0], [12.7, 948.0], [12.8, 948.0], [12.9, 948.0], [13.0, 949.0], [13.1, 949.0], [13.2, 949.0], [13.3, 949.0], [13.4, 949.0], [13.5, 949.0], [13.6, 949.0], [13.7, 949.0], [13.8, 949.0], [13.9, 949.0], [14.0, 951.0], [14.1, 951.0], [14.2, 951.0], [14.3, 951.0], [14.4, 951.0], [14.5, 951.0], [14.6, 951.0], [14.7, 951.0], [14.8, 951.0], [14.9, 951.0], [15.0, 954.0], [15.1, 954.0], [15.2, 954.0], [15.3, 954.0], [15.4, 954.0], [15.5, 954.0], [15.6, 954.0], [15.7, 954.0], [15.8, 954.0], [15.9, 954.0], [16.0, 955.0], [16.1, 955.0], [16.2, 955.0], [16.3, 955.0], [16.4, 955.0], [16.5, 955.0], [16.6, 955.0], [16.7, 955.0], [16.8, 955.0], [16.9, 955.0], [17.0, 957.0], [17.1, 957.0], [17.2, 957.0], [17.3, 957.0], [17.4, 957.0], [17.5, 957.0], [17.6, 957.0], [17.7, 957.0], [17.8, 957.0], [17.9, 957.0], [18.0, 961.0], [18.1, 961.0], [18.2, 961.0], [18.3, 961.0], [18.4, 961.0], [18.5, 961.0], [18.6, 961.0], [18.7, 961.0], [18.8, 961.0], [18.9, 961.0], [19.0, 967.0], [19.1, 967.0], [19.2, 967.0], [19.3, 967.0], [19.4, 967.0], [19.5, 967.0], [19.6, 967.0], [19.7, 967.0], [19.8, 967.0], [19.9, 967.0], [20.0, 969.0], [20.1, 969.0], [20.2, 969.0], [20.3, 969.0], [20.4, 969.0], [20.5, 969.0], [20.6, 969.0], [20.7, 969.0], [20.8, 969.0], [20.9, 969.0], [21.0, 970.0], [21.1, 970.0], [21.2, 970.0], [21.3, 970.0], [21.4, 970.0], [21.5, 970.0], [21.6, 970.0], [21.7, 970.0], [21.8, 970.0], [21.9, 970.0], [22.0, 977.0], [22.1, 977.0], [22.2, 977.0], [22.3, 977.0], [22.4, 977.0], [22.5, 977.0], [22.6, 977.0], [22.7, 977.0], [22.8, 977.0], [22.9, 977.0], [23.0, 980.0], [23.1, 980.0], [23.2, 980.0], [23.3, 980.0], [23.4, 980.0], [23.5, 980.0], [23.6, 980.0], [23.7, 980.0], [23.8, 980.0], [23.9, 980.0], [24.0, 984.0], [24.1, 984.0], [24.2, 984.0], [24.3, 984.0], [24.4, 984.0], [24.5, 984.0], [24.6, 984.0], [24.7, 984.0], [24.8, 984.0], [24.9, 984.0], [25.0, 988.0], [25.1, 988.0], [25.2, 988.0], [25.3, 988.0], [25.4, 988.0], [25.5, 988.0], [25.6, 988.0], [25.7, 988.0], [25.8, 988.0], [25.9, 988.0], [26.0, 991.0], [26.1, 991.0], [26.2, 991.0], [26.3, 991.0], [26.4, 991.0], [26.5, 991.0], [26.6, 991.0], [26.7, 991.0], [26.8, 991.0], [26.9, 991.0], [27.0, 999.0], [27.1, 999.0], [27.2, 999.0], [27.3, 999.0], [27.4, 999.0], [27.5, 999.0], [27.6, 999.0], [27.7, 999.0], [27.8, 999.0], [27.9, 999.0], [28.0, 1001.0], [28.1, 1001.0], [28.2, 1001.0], [28.3, 1001.0], [28.4, 1001.0], [28.5, 1001.0], [28.6, 1001.0], [28.7, 1001.0], [28.8, 1001.0], [28.9, 1001.0], [29.0, 1001.0], [29.1, 1001.0], [29.2, 1001.0], [29.3, 1001.0], [29.4, 1001.0], [29.5, 1001.0], [29.6, 1001.0], [29.7, 1001.0], [29.8, 1001.0], [29.9, 1001.0], [30.0, 1007.0], [30.1, 1007.0], [30.2, 1007.0], [30.3, 1007.0], [30.4, 1007.0], [30.5, 1007.0], [30.6, 1007.0], [30.7, 1007.0], [30.8, 1007.0], [30.9, 1007.0], [31.0, 1008.0], [31.1, 1008.0], [31.2, 1008.0], [31.3, 1008.0], [31.4, 1008.0], [31.5, 1008.0], [31.6, 1008.0], [31.7, 1008.0], [31.8, 1008.0], [31.9, 1008.0], [32.0, 1013.0], [32.1, 1013.0], [32.2, 1013.0], [32.3, 1013.0], [32.4, 1013.0], [32.5, 1013.0], [32.6, 1013.0], [32.7, 1013.0], [32.8, 1013.0], [32.9, 1013.0], [33.0, 1025.0], [33.1, 1025.0], [33.2, 1025.0], [33.3, 1025.0], [33.4, 1025.0], [33.5, 1025.0], [33.6, 1025.0], [33.7, 1025.0], [33.8, 1025.0], [33.9, 1025.0], [34.0, 1026.0], [34.1, 1026.0], [34.2, 1026.0], [34.3, 1026.0], [34.4, 1026.0], [34.5, 1026.0], [34.6, 1026.0], [34.7, 1026.0], [34.8, 1026.0], [34.9, 1026.0], [35.0, 1029.0], [35.1, 1029.0], [35.2, 1029.0], [35.3, 1029.0], [35.4, 1029.0], [35.5, 1029.0], [35.6, 1029.0], [35.7, 1029.0], [35.8, 1029.0], [35.9, 1029.0], [36.0, 1044.0], [36.1, 1044.0], [36.2, 1044.0], [36.3, 1044.0], [36.4, 1044.0], [36.5, 1044.0], [36.6, 1044.0], [36.7, 1044.0], [36.8, 1044.0], [36.9, 1044.0], [37.0, 1048.0], [37.1, 1048.0], [37.2, 1048.0], [37.3, 1048.0], [37.4, 1048.0], [37.5, 1048.0], [37.6, 1048.0], [37.7, 1048.0], [37.8, 1048.0], [37.9, 1048.0], [38.0, 1049.0], [38.1, 1049.0], [38.2, 1049.0], [38.3, 1049.0], [38.4, 1049.0], [38.5, 1049.0], [38.6, 1049.0], [38.7, 1049.0], [38.8, 1049.0], [38.9, 1049.0], [39.0, 1051.0], [39.1, 1051.0], [39.2, 1051.0], [39.3, 1051.0], [39.4, 1051.0], [39.5, 1051.0], [39.6, 1051.0], [39.7, 1051.0], [39.8, 1051.0], [39.9, 1051.0], [40.0, 1052.0], [40.1, 1052.0], [40.2, 1052.0], [40.3, 1052.0], [40.4, 1052.0], [40.5, 1052.0], [40.6, 1052.0], [40.7, 1052.0], [40.8, 1052.0], [40.9, 1052.0], [41.0, 1053.0], [41.1, 1053.0], [41.2, 1053.0], [41.3, 1053.0], [41.4, 1053.0], [41.5, 1053.0], [41.6, 1053.0], [41.7, 1053.0], [41.8, 1053.0], [41.9, 1053.0], [42.0, 1068.0], [42.1, 1068.0], [42.2, 1068.0], [42.3, 1068.0], [42.4, 1068.0], [42.5, 1068.0], [42.6, 1068.0], [42.7, 1068.0], [42.8, 1068.0], [42.9, 1068.0], [43.0, 1072.0], [43.1, 1072.0], [43.2, 1072.0], [43.3, 1072.0], [43.4, 1072.0], [43.5, 1072.0], [43.6, 1072.0], [43.7, 1072.0], [43.8, 1072.0], [43.9, 1072.0], [44.0, 1074.0], [44.1, 1074.0], [44.2, 1074.0], [44.3, 1074.0], [44.4, 1074.0], [44.5, 1074.0], [44.6, 1074.0], [44.7, 1074.0], [44.8, 1074.0], [44.9, 1074.0], [45.0, 1080.0], [45.1, 1080.0], [45.2, 1080.0], [45.3, 1080.0], [45.4, 1080.0], [45.5, 1080.0], [45.6, 1080.0], [45.7, 1080.0], [45.8, 1080.0], [45.9, 1080.0], [46.0, 1082.0], [46.1, 1082.0], [46.2, 1082.0], [46.3, 1082.0], [46.4, 1082.0], [46.5, 1082.0], [46.6, 1082.0], [46.7, 1082.0], [46.8, 1082.0], [46.9, 1082.0], [47.0, 1087.0], [47.1, 1087.0], [47.2, 1087.0], [47.3, 1087.0], [47.4, 1087.0], [47.5, 1087.0], [47.6, 1087.0], [47.7, 1087.0], [47.8, 1087.0], [47.9, 1087.0], [48.0, 1096.0], [48.1, 1096.0], [48.2, 1096.0], [48.3, 1096.0], [48.4, 1096.0], [48.5, 1096.0], [48.6, 1096.0], [48.7, 1096.0], [48.8, 1096.0], [48.9, 1096.0], [49.0, 1099.0], [49.1, 1099.0], [49.2, 1099.0], [49.3, 1099.0], [49.4, 1099.0], [49.5, 1099.0], [49.6, 1099.0], [49.7, 1099.0], [49.8, 1099.0], [49.9, 1099.0], [50.0, 1105.0], [50.1, 1105.0], [50.2, 1105.0], [50.3, 1105.0], [50.4, 1105.0], [50.5, 1105.0], [50.6, 1105.0], [50.7, 1105.0], [50.8, 1105.0], [50.9, 1105.0], [51.0, 1106.0], [51.1, 1106.0], [51.2, 1106.0], [51.3, 1106.0], [51.4, 1106.0], [51.5, 1106.0], [51.6, 1106.0], [51.7, 1106.0], [51.8, 1106.0], [51.9, 1106.0], [52.0, 1107.0], [52.1, 1107.0], [52.2, 1107.0], [52.3, 1107.0], [52.4, 1107.0], [52.5, 1107.0], [52.6, 1107.0], [52.7, 1107.0], [52.8, 1107.0], [52.9, 1107.0], [53.0, 1109.0], [53.1, 1109.0], [53.2, 1109.0], [53.3, 1109.0], [53.4, 1109.0], [53.5, 1109.0], [53.6, 1109.0], [53.7, 1109.0], [53.8, 1109.0], [53.9, 1109.0], [54.0, 1118.0], [54.1, 1118.0], [54.2, 1118.0], [54.3, 1118.0], [54.4, 1118.0], [54.5, 1118.0], [54.6, 1118.0], [54.7, 1118.0], [54.8, 1118.0], [54.9, 1118.0], [55.0, 1143.0], [55.1, 1143.0], [55.2, 1143.0], [55.3, 1143.0], [55.4, 1143.0], [55.5, 1143.0], [55.6, 1143.0], [55.7, 1143.0], [55.8, 1143.0], [55.9, 1143.0], [56.0, 1145.0], [56.1, 1145.0], [56.2, 1145.0], [56.3, 1145.0], [56.4, 1145.0], [56.5, 1145.0], [56.6, 1145.0], [56.7, 1145.0], [56.8, 1145.0], [56.9, 1145.0], [57.0, 1149.0], [57.1, 1149.0], [57.2, 1149.0], [57.3, 1149.0], [57.4, 1149.0], [57.5, 1149.0], [57.6, 1149.0], [57.7, 1149.0], [57.8, 1149.0], [57.9, 1149.0], [58.0, 1151.0], [58.1, 1151.0], [58.2, 1151.0], [58.3, 1151.0], [58.4, 1151.0], [58.5, 1151.0], [58.6, 1151.0], [58.7, 1151.0], [58.8, 1151.0], [58.9, 1151.0], [59.0, 1157.0], [59.1, 1157.0], [59.2, 1157.0], [59.3, 1157.0], [59.4, 1157.0], [59.5, 1157.0], [59.6, 1157.0], [59.7, 1157.0], [59.8, 1157.0], [59.9, 1157.0], [60.0, 1158.0], [60.1, 1158.0], [60.2, 1158.0], [60.3, 1158.0], [60.4, 1158.0], [60.5, 1158.0], [60.6, 1158.0], [60.7, 1158.0], [60.8, 1158.0], [60.9, 1158.0], [61.0, 1159.0], [61.1, 1159.0], [61.2, 1159.0], [61.3, 1159.0], [61.4, 1159.0], [61.5, 1159.0], [61.6, 1159.0], [61.7, 1159.0], [61.8, 1159.0], [61.9, 1159.0], [62.0, 1163.0], [62.1, 1163.0], [62.2, 1163.0], [62.3, 1163.0], [62.4, 1163.0], [62.5, 1163.0], [62.6, 1163.0], [62.7, 1163.0], [62.8, 1163.0], [62.9, 1163.0], [63.0, 1178.0], [63.1, 1178.0], [63.2, 1178.0], [63.3, 1178.0], [63.4, 1178.0], [63.5, 1178.0], [63.6, 1178.0], [63.7, 1178.0], [63.8, 1178.0], [63.9, 1178.0], [64.0, 1182.0], [64.1, 1182.0], [64.2, 1182.0], [64.3, 1182.0], [64.4, 1182.0], [64.5, 1182.0], [64.6, 1182.0], [64.7, 1182.0], [64.8, 1182.0], [64.9, 1182.0], [65.0, 1185.0], [65.1, 1185.0], [65.2, 1185.0], [65.3, 1185.0], [65.4, 1185.0], [65.5, 1185.0], [65.6, 1185.0], [65.7, 1185.0], [65.8, 1185.0], [65.9, 1185.0], [66.0, 1186.0], [66.1, 1186.0], [66.2, 1186.0], [66.3, 1186.0], [66.4, 1186.0], [66.5, 1186.0], [66.6, 1186.0], [66.7, 1186.0], [66.8, 1186.0], [66.9, 1186.0], [67.0, 1190.0], [67.1, 1190.0], [67.2, 1190.0], [67.3, 1190.0], [67.4, 1190.0], [67.5, 1190.0], [67.6, 1190.0], [67.7, 1190.0], [67.8, 1190.0], [67.9, 1190.0], [68.0, 1190.0], [68.1, 1190.0], [68.2, 1190.0], [68.3, 1190.0], [68.4, 1190.0], [68.5, 1190.0], [68.6, 1190.0], [68.7, 1190.0], [68.8, 1190.0], [68.9, 1190.0], [69.0, 1191.0], [69.1, 1191.0], [69.2, 1191.0], [69.3, 1191.0], [69.4, 1191.0], [69.5, 1191.0], [69.6, 1191.0], [69.7, 1191.0], [69.8, 1191.0], [69.9, 1191.0], [70.0, 1200.0], [70.1, 1200.0], [70.2, 1200.0], [70.3, 1200.0], [70.4, 1200.0], [70.5, 1200.0], [70.6, 1200.0], [70.7, 1200.0], [70.8, 1200.0], [70.9, 1200.0], [71.0, 1214.0], [71.1, 1214.0], [71.2, 1214.0], [71.3, 1214.0], [71.4, 1214.0], [71.5, 1214.0], [71.6, 1214.0], [71.7, 1214.0], [71.8, 1214.0], [71.9, 1214.0], [72.0, 1214.0], [72.1, 1214.0], [72.2, 1214.0], [72.3, 1214.0], [72.4, 1214.0], [72.5, 1214.0], [72.6, 1214.0], [72.7, 1214.0], [72.8, 1214.0], [72.9, 1214.0], [73.0, 1222.0], [73.1, 1222.0], [73.2, 1222.0], [73.3, 1222.0], [73.4, 1222.0], [73.5, 1222.0], [73.6, 1222.0], [73.7, 1222.0], [73.8, 1222.0], [73.9, 1222.0], [74.0, 1225.0], [74.1, 1225.0], [74.2, 1225.0], [74.3, 1225.0], [74.4, 1225.0], [74.5, 1225.0], [74.6, 1225.0], [74.7, 1225.0], [74.8, 1225.0], [74.9, 1225.0], [75.0, 1229.0], [75.1, 1229.0], [75.2, 1229.0], [75.3, 1229.0], [75.4, 1229.0], [75.5, 1229.0], [75.6, 1229.0], [75.7, 1229.0], [75.8, 1229.0], [75.9, 1229.0], [76.0, 1232.0], [76.1, 1232.0], [76.2, 1232.0], [76.3, 1232.0], [76.4, 1232.0], [76.5, 1232.0], [76.6, 1232.0], [76.7, 1232.0], [76.8, 1232.0], [76.9, 1232.0], [77.0, 1232.0], [77.1, 1232.0], [77.2, 1232.0], [77.3, 1232.0], [77.4, 1232.0], [77.5, 1232.0], [77.6, 1232.0], [77.7, 1232.0], [77.8, 1232.0], [77.9, 1232.0], [78.0, 1235.0], [78.1, 1235.0], [78.2, 1235.0], [78.3, 1235.0], [78.4, 1235.0], [78.5, 1235.0], [78.6, 1235.0], [78.7, 1235.0], [78.8, 1235.0], [78.9, 1235.0], [79.0, 1241.0], [79.1, 1241.0], [79.2, 1241.0], [79.3, 1241.0], [79.4, 1241.0], [79.5, 1241.0], [79.6, 1241.0], [79.7, 1241.0], [79.8, 1241.0], [79.9, 1241.0], [80.0, 1241.0], [80.1, 1241.0], [80.2, 1241.0], [80.3, 1241.0], [80.4, 1241.0], [80.5, 1241.0], [80.6, 1241.0], [80.7, 1241.0], [80.8, 1241.0], [80.9, 1241.0], [81.0, 1241.0], [81.1, 1241.0], [81.2, 1241.0], [81.3, 1241.0], [81.4, 1241.0], [81.5, 1241.0], [81.6, 1241.0], [81.7, 1241.0], [81.8, 1241.0], [81.9, 1241.0], [82.0, 1261.0], [82.1, 1261.0], [82.2, 1261.0], [82.3, 1261.0], [82.4, 1261.0], [82.5, 1261.0], [82.6, 1261.0], [82.7, 1261.0], [82.8, 1261.0], [82.9, 1261.0], [83.0, 1266.0], [83.1, 1266.0], [83.2, 1266.0], [83.3, 1266.0], [83.4, 1266.0], [83.5, 1266.0], [83.6, 1266.0], [83.7, 1266.0], [83.8, 1266.0], [83.9, 1266.0], [84.0, 1271.0], [84.1, 1271.0], [84.2, 1271.0], [84.3, 1271.0], [84.4, 1271.0], [84.5, 1271.0], [84.6, 1271.0], [84.7, 1271.0], [84.8, 1271.0], [84.9, 1271.0], [85.0, 1278.0], [85.1, 1278.0], [85.2, 1278.0], [85.3, 1278.0], [85.4, 1278.0], [85.5, 1278.0], [85.6, 1278.0], [85.7, 1278.0], [85.8, 1278.0], [85.9, 1278.0], [86.0, 1278.0], [86.1, 1278.0], [86.2, 1278.0], [86.3, 1278.0], [86.4, 1278.0], [86.5, 1278.0], [86.6, 1278.0], [86.7, 1278.0], [86.8, 1278.0], [86.9, 1278.0], [87.0, 1280.0], [87.1, 1280.0], [87.2, 1280.0], [87.3, 1280.0], [87.4, 1280.0], [87.5, 1280.0], [87.6, 1280.0], [87.7, 1280.0], [87.8, 1280.0], [87.9, 1280.0], [88.0, 1281.0], [88.1, 1281.0], [88.2, 1281.0], [88.3, 1281.0], [88.4, 1281.0], [88.5, 1281.0], [88.6, 1281.0], [88.7, 1281.0], [88.8, 1281.0], [88.9, 1281.0], [89.0, 1289.0], [89.1, 1289.0], [89.2, 1289.0], [89.3, 1289.0], [89.4, 1289.0], [89.5, 1289.0], [89.6, 1289.0], [89.7, 1289.0], [89.8, 1289.0], [89.9, 1289.0], [90.0, 1302.0], [90.1, 1302.0], [90.2, 1302.0], [90.3, 1302.0], [90.4, 1302.0], [90.5, 1302.0], [90.6, 1302.0], [90.7, 1302.0], [90.8, 1302.0], [90.9, 1302.0], [91.0, 1308.0], [91.1, 1308.0], [91.2, 1308.0], [91.3, 1308.0], [91.4, 1308.0], [91.5, 1308.0], [91.6, 1308.0], [91.7, 1308.0], [91.8, 1308.0], [91.9, 1308.0], [92.0, 1316.0], [92.1, 1316.0], [92.2, 1316.0], [92.3, 1316.0], [92.4, 1316.0], [92.5, 1316.0], [92.6, 1316.0], [92.7, 1316.0], [92.8, 1316.0], [92.9, 1316.0], [93.0, 1321.0], [93.1, 1321.0], [93.2, 1321.0], [93.3, 1321.0], [93.4, 1321.0], [93.5, 1321.0], [93.6, 1321.0], [93.7, 1321.0], [93.8, 1321.0], [93.9, 1321.0], [94.0, 1330.0], [94.1, 1330.0], [94.2, 1330.0], [94.3, 1330.0], [94.4, 1330.0], [94.5, 1330.0], [94.6, 1330.0], [94.7, 1330.0], [94.8, 1330.0], [94.9, 1330.0], [95.0, 1348.0], [95.1, 1348.0], [95.2, 1348.0], [95.3, 1348.0], [95.4, 1348.0], [95.5, 1348.0], [95.6, 1348.0], [95.7, 1348.0], [95.8, 1348.0], [95.9, 1348.0], [96.0, 1384.0], [96.1, 1384.0], [96.2, 1384.0], [96.3, 1384.0], [96.4, 1384.0], [96.5, 1384.0], [96.6, 1384.0], [96.7, 1384.0], [96.8, 1384.0], [96.9, 1384.0], [97.0, 1397.0], [97.1, 1397.0], [97.2, 1397.0], [97.3, 1397.0], [97.4, 1397.0], [97.5, 1397.0], [97.6, 1397.0], [97.7, 1397.0], [97.8, 1397.0], [97.9, 1397.0], [98.0, 1412.0], [98.1, 1412.0], [98.2, 1412.0], [98.3, 1412.0], [98.4, 1412.0], [98.5, 1412.0], [98.6, 1412.0], [98.7, 1412.0], [98.8, 1412.0], [98.9, 1412.0], [99.0, 1513.0], [99.1, 1513.0], [99.2, 1513.0], [99.3, 1513.0], [99.4, 1513.0], [99.5, 1513.0], [99.6, 1513.0], [99.7, 1513.0], [99.8, 1513.0], [99.9, 1513.0]], "isOverall": false, "label": "Excecute Login", "isController": false}], "supportsControllersDiscrimination": true, "maxX": 100.0, "title": "Response Time Percentiles"}},
        getOptions: function() {
            return {
                series: {
                    points: { show: false }
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimePercentiles'
                },
                xaxis: {
                    tickDecimals: 1,
                    axisLabel: "Percentiles",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Percentile value in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : %x.2 percentile was %y ms"
                },
                selection: { mode: "xy" },
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesResponseTimePercentiles"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimesPercentiles"), dataset, options);
            // setup overview
            $.plot($("#overviewResponseTimesPercentiles"), dataset, prepareOverviewOptions(options));
        }
};

/**
 * @param elementId Id of element where we display message
 */
function setEmptyGraph(elementId) {
    $(function() {
        $(elementId).text("No graph series with filter="+seriesFilter);
    });
}

// Response times percentiles
function refreshResponseTimePercentiles() {
    var infos = responseTimePercentilesInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyResponseTimePercentiles");
        return;
    }
    if (isGraph($("#flotResponseTimesPercentiles"))){
        infos.createGraph();
    } else {
        var choiceContainer = $("#choicesResponseTimePercentiles");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimesPercentiles", "#overviewResponseTimesPercentiles");
        $('#bodyResponseTimePercentiles .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
}

var responseTimeDistributionInfos = {
        data: {"result": {"minY": 1.0, "minX": 500.0, "maxY": 22.0, "series": [{"data": [[1100.0, 20.0], [600.0, 2.0], [1200.0, 20.0], [1300.0, 8.0], [700.0, 2.0], [1400.0, 1.0], [1500.0, 1.0], [800.0, 4.0], [900.0, 19.0], [500.0, 1.0], [1000.0, 22.0]], "isOverall": false, "label": "Excecute Login", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 100, "maxX": 1500.0, "title": "Response Time Distribution"}},
        getOptions: function() {
            var granularity = this.data.result.granularity;
            return {
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimeDistribution'
                },
                xaxis:{
                    axisLabel: "Response times in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of responses",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                bars : {
                    show: true,
                    barWidth: this.data.result.granularity
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: function(label, xval, yval, flotItem){
                        return yval + " responses for " + label + " were between " + xval + " and " + (xval + granularity) + " ms";
                    }
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimeDistribution"), prepareData(data.result.series, $("#choicesResponseTimeDistribution")), options);
        }

};

// Response time distribution
function refreshResponseTimeDistribution() {
    var infos = responseTimeDistributionInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyResponseTimeDistribution");
        return;
    }
    if (isGraph($("#flotResponseTimeDistribution"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesResponseTimeDistribution");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        $('#footerResponseTimeDistribution .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};


var syntheticResponseTimeDistributionInfos = {
        data: {"result": {"minY": 1.0, "minX": 1.0, "ticks": [[0, "Requests having \nresponse time <= 500ms"], [1, "Requests having \nresponse time > 500ms and <= 1,500ms"], [2, "Requests having \nresponse time > 1,500ms"], [3, "Requests in error"]], "maxY": 99.0, "series": [{"data": [], "color": "#9ACD32", "isOverall": false, "label": "Requests having \nresponse time <= 500ms", "isController": false}, {"data": [[1.0, 99.0]], "color": "yellow", "isOverall": false, "label": "Requests having \nresponse time > 500ms and <= 1,500ms", "isController": false}, {"data": [[2.0, 1.0]], "color": "orange", "isOverall": false, "label": "Requests having \nresponse time > 1,500ms", "isController": false}, {"data": [], "color": "#FF6347", "isOverall": false, "label": "Requests in error", "isController": false}], "supportsControllersDiscrimination": false, "maxX": 2.0, "title": "Synthetic Response Times Distribution"}},
        getOptions: function() {
            return {
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendSyntheticResponseTimeDistribution'
                },
                xaxis:{
                    axisLabel: "Response times ranges",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                    tickLength:0,
                    min:-0.5,
                    max:3.5
                },
                yaxis: {
                    axisLabel: "Number of responses",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                bars : {
                    show: true,
                    align: "center",
                    barWidth: 0.25,
                    fill:.75
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: function(label, xval, yval, flotItem){
                        return yval + " " + label;
                    }
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var options = this.getOptions();
            prepareOptions(options, data);
            options.xaxis.ticks = data.result.ticks;
            $.plot($("#flotSyntheticResponseTimeDistribution"), prepareData(data.result.series, $("#choicesSyntheticResponseTimeDistribution")), options);
        }

};

// Response time distribution
function refreshSyntheticResponseTimeDistribution() {
    var infos = syntheticResponseTimeDistributionInfos;
    prepareSeries(infos.data, true);
    if (isGraph($("#flotSyntheticResponseTimeDistribution"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesSyntheticResponseTimeDistribution");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        $('#footerSyntheticResponseTimeDistribution .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var activeThreadsOverTimeInfos = {
        data: {"result": {"minY": 47.5, "minX": 1.65636546E12, "maxY": 47.5, "series": [{"data": [[1.65636546E12, 47.5]], "isOverall": false, "label": "Login-100", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.65636546E12, "title": "Active Threads Over Time"}},
        getOptions: function() {
            return {
                series: {
                    stack: true,
                    lines: {
                        show: true,
                        fill: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of active threads",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 6,
                    show: true,
                    container: '#legendActiveThreadsOverTime'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                selection: {
                    mode: 'xy'
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : At %x there were %y active threads"
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesActiveThreadsOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotActiveThreadsOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewActiveThreadsOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Active Threads Over Time
function refreshActiveThreadsOverTime(fixTimestamps) {
    var infos = activeThreadsOverTimeInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotActiveThreadsOverTime"))) {
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesActiveThreadsOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotActiveThreadsOverTime", "#overviewActiveThreadsOverTime");
        $('#footerActiveThreadsOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var timeVsThreadsInfos = {
        data: {"result": {"minY": 688.0, "minX": 1.0, "maxY": 1513.0, "series": [{"data": [[3.0, 1241.0], [4.0, 1235.0], [5.0, 1232.0], [6.0, 1222.0], [7.0, 1214.0], [8.0, 1191.0], [9.0, 1178.0], [10.0, 1232.0], [11.0, 1225.0], [12.0, 1074.0], [13.0, 1107.0], [15.0, 1148.0], [16.0, 1316.0], [18.0, 1205.5], [19.0, 1280.0], [20.0, 1099.0], [21.0, 1241.0], [22.0, 1330.0], [23.0, 1302.0], [24.0, 1348.0], [25.0, 1384.0], [26.0, 1397.0], [27.0, 1261.0], [29.0, 1001.0], [30.0, 1052.0], [31.0, 1007.0], [33.0, 1082.0], [32.0, 999.0], [34.0, 1281.0], [37.0, 1080.0], [36.0, 1050.5], [39.0, 1097.0], [41.0, 1157.0], [40.0, 1190.0], [43.0, 1214.0], [42.0, 949.0], [45.0, 1266.0], [44.0, 1278.0], [47.0, 1308.0], [46.0, 1513.0], [48.0, 915.0], [49.0, 1029.0], [51.0, 1412.0], [50.0, 1068.0], [53.0, 1186.0], [52.0, 1151.0], [54.0, 908.0], [55.0, 991.0], [57.0, 1163.0], [56.0, 1025.0], [59.0, 688.0], [58.0, 1278.0], [61.0, 716.0], [63.0, 1189.4], [65.0, 939.0], [67.0, 892.5], [66.0, 1149.0], [64.0, 1143.0], [71.0, 1096.0], [70.0, 1118.0], [69.0, 969.0], [68.0, 1087.0], [75.0, 929.6666666666666], [74.0, 970.0], [73.0, 832.0], [72.0, 826.0], [77.0, 952.5], [79.0, 992.5], [78.0, 969.0], [76.0, 961.0], [80.0, 965.5], [83.0, 1072.0], [82.0, 1013.0], [81.0, 918.0], [87.0, 1051.0], [86.0, 800.0], [85.0, 988.0], [84.0, 1026.0], [88.0, 1044.0], [1.0, 1271.0]], "isOverall": false, "label": "Excecute Login", "isController": false}, {"data": [[47.5, 1095.7499999999993]], "isOverall": false, "label": "Excecute Login-Aggregated", "isController": false}], "supportsControllersDiscrimination": true, "maxX": 88.0, "title": "Time VS Threads"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    axisLabel: "Number of active threads",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average response times in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: { noColumns: 2,show: true, container: '#legendTimeVsThreads' },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s: At %x.2 active threads, Average response time was %y.2 ms"
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesTimeVsThreads"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotTimesVsThreads"), dataset, options);
            // setup overview
            $.plot($("#overviewTimesVsThreads"), dataset, prepareOverviewOptions(options));
        }
};

// Time vs threads
function refreshTimeVsThreads(){
    var infos = timeVsThreadsInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyTimeVsThreads");
        return;
    }
    if(isGraph($("#flotTimesVsThreads"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesTimeVsThreads");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotTimesVsThreads", "#overviewTimesVsThreads");
        $('#footerTimeVsThreads .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var bytesThroughputOverTimeInfos = {
        data : {"result": {"minY": 426.6666666666667, "minX": 1.65636546E12, "maxY": 643.3333333333334, "series": [{"data": [[1.65636546E12, 643.3333333333334]], "isOverall": false, "label": "Bytes received per second", "isController": false}, {"data": [[1.65636546E12, 426.6666666666667]], "isOverall": false, "label": "Bytes sent per second", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.65636546E12, "title": "Bytes Throughput Over Time"}},
        getOptions : function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity) ,
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Bytes / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendBytesThroughputOverTime'
                },
                selection: {
                    mode: "xy"
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y"
                }
            };
        },
        createGraph : function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesBytesThroughputOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotBytesThroughputOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewBytesThroughputOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Bytes throughput Over Time
function refreshBytesThroughputOverTime(fixTimestamps) {
    var infos = bytesThroughputOverTimeInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotBytesThroughputOverTime"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesBytesThroughputOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotBytesThroughputOverTime", "#overviewBytesThroughputOverTime");
        $('#footerBytesThroughputOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
}

var responseTimesOverTimeInfos = {
        data: {"result": {"minY": 1095.7499999999993, "minX": 1.65636546E12, "maxY": 1095.7499999999993, "series": [{"data": [[1.65636546E12, 1095.7499999999993]], "isOverall": false, "label": "Excecute Login", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.65636546E12, "title": "Response Time Over Time"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average response time in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimesOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Average response time was %y ms"
                }
            };
        },
        createGraph: function() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesResponseTimesOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimesOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewResponseTimesOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Response Times Over Time
function refreshResponseTimeOverTime(fixTimestamps) {
    var infos = responseTimesOverTimeInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyResponseTimeOverTime");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotResponseTimesOverTime"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesResponseTimesOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimesOverTime", "#overviewResponseTimesOverTime");
        $('#footerResponseTimesOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var latenciesOverTimeInfos = {
        data: {"result": {"minY": 1095.5699999999997, "minX": 1.65636546E12, "maxY": 1095.5699999999997, "series": [{"data": [[1.65636546E12, 1095.5699999999997]], "isOverall": false, "label": "Excecute Login", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.65636546E12, "title": "Latencies Over Time"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average response latencies in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendLatenciesOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Average latency was %y ms"
                }
            };
        },
        createGraph: function () {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesLatenciesOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotLatenciesOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewLatenciesOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Latencies Over Time
function refreshLatenciesOverTime(fixTimestamps) {
    var infos = latenciesOverTimeInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyLatenciesOverTime");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotLatenciesOverTime"))) {
        infos.createGraph();
    }else {
        var choiceContainer = $("#choicesLatenciesOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotLatenciesOverTime", "#overviewLatenciesOverTime");
        $('#footerLatenciesOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var connectTimeOverTimeInfos = {
        data: {"result": {"minY": 6.099999999999999, "minX": 1.65636546E12, "maxY": 6.099999999999999, "series": [{"data": [[1.65636546E12, 6.099999999999999]], "isOverall": false, "label": "Excecute Login", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.65636546E12, "title": "Connect Time Over Time"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getConnectTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Average Connect Time in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendConnectTimeOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Average connect time was %y ms"
                }
            };
        },
        createGraph: function () {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesConnectTimeOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotConnectTimeOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewConnectTimeOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Connect Time Over Time
function refreshConnectTimeOverTime(fixTimestamps) {
    var infos = connectTimeOverTimeInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyConnectTimeOverTime");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotConnectTimeOverTime"))) {
        infos.createGraph();
    }else {
        var choiceContainer = $("#choicesConnectTimeOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotConnectTimeOverTime", "#overviewConnectTimeOverTime");
        $('#footerConnectTimeOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var responseTimePercentilesOverTimeInfos = {
        data: {"result": {"minY": 541.0, "minX": 1.65636546E12, "maxY": 1513.0, "series": [{"data": [[1.65636546E12, 1513.0]], "isOverall": false, "label": "Max", "isController": false}, {"data": [[1.65636546E12, 1300.7]], "isOverall": false, "label": "90th percentile", "isController": false}, {"data": [[1.65636546E12, 1511.9899999999996]], "isOverall": false, "label": "99th percentile", "isController": false}, {"data": [[1.65636546E12, 1347.1]], "isOverall": false, "label": "95th percentile", "isController": false}, {"data": [[1.65636546E12, 541.0]], "isOverall": false, "label": "Min", "isController": false}, {"data": [[1.65636546E12, 1102.0]], "isOverall": false, "label": "Median", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.65636546E12, "title": "Response Time Percentiles Over Time (successful requests only)"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true,
                        fill: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Response Time in ms",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: '#legendResponseTimePercentilesOverTime'
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s : at %x Response time was %y ms"
                }
            };
        },
        createGraph: function () {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesResponseTimePercentilesOverTime"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotResponseTimePercentilesOverTime"), dataset, options);
            // setup overview
            $.plot($("#overviewResponseTimePercentilesOverTime"), dataset, prepareOverviewOptions(options));
        }
};

// Response Time Percentiles Over Time
function refreshResponseTimePercentilesOverTime(fixTimestamps) {
    var infos = responseTimePercentilesOverTimeInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotResponseTimePercentilesOverTime"))) {
        infos.createGraph();
    }else {
        var choiceContainer = $("#choicesResponseTimePercentilesOverTime");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimePercentilesOverTime", "#overviewResponseTimePercentilesOverTime");
        $('#footerResponseTimePercentilesOverTime .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};


var responseTimeVsRequestInfos = {
    data: {"result": {"minY": 917.0, "minX": 11.0, "maxY": 1232.0, "series": [{"data": [[17.0, 917.0], [72.0, 1108.0], [11.0, 1232.0]], "isOverall": false, "label": "Successes", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 1000, "maxX": 72.0, "title": "Response Time Vs Request"}},
    getOptions: function() {
        return {
            series: {
                lines: {
                    show: false
                },
                points: {
                    show: true
                }
            },
            xaxis: {
                axisLabel: "Global number of requests per second",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            yaxis: {
                axisLabel: "Median Response Time in ms",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            legend: {
                noColumns: 2,
                show: true,
                container: '#legendResponseTimeVsRequest'
            },
            selection: {
                mode: 'xy'
            },
            grid: {
                hoverable: true // IMPORTANT! this is needed for tooltip to work
            },
            tooltip: true,
            tooltipOpts: {
                content: "%s : Median response time at %x req/s was %y ms"
            },
            colors: ["#9ACD32", "#FF6347"]
        };
    },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesResponseTimeVsRequest"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotResponseTimeVsRequest"), dataset, options);
        // setup overview
        $.plot($("#overviewResponseTimeVsRequest"), dataset, prepareOverviewOptions(options));

    }
};

// Response Time vs Request
function refreshResponseTimeVsRequest() {
    var infos = responseTimeVsRequestInfos;
    prepareSeries(infos.data);
    if (isGraph($("#flotResponseTimeVsRequest"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesResponseTimeVsRequest");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotResponseTimeVsRequest", "#overviewResponseTimeVsRequest");
        $('#footerResponseRimeVsRequest .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};


var latenciesVsRequestInfos = {
    data: {"result": {"minY": 917.0, "minX": 11.0, "maxY": 1232.0, "series": [{"data": [[17.0, 917.0], [72.0, 1108.0], [11.0, 1232.0]], "isOverall": false, "label": "Successes", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 1000, "maxX": 72.0, "title": "Latencies Vs Request"}},
    getOptions: function() {
        return{
            series: {
                lines: {
                    show: false
                },
                points: {
                    show: true
                }
            },
            xaxis: {
                axisLabel: "Global number of requests per second",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            yaxis: {
                axisLabel: "Median Latency in ms",
                axisLabelUseCanvas: true,
                axisLabelFontSizePixels: 12,
                axisLabelFontFamily: 'Verdana, Arial',
                axisLabelPadding: 20,
            },
            legend: { noColumns: 2,show: true, container: '#legendLatencyVsRequest' },
            selection: {
                mode: 'xy'
            },
            grid: {
                hoverable: true // IMPORTANT! this is needed for tooltip to work
            },
            tooltip: true,
            tooltipOpts: {
                content: "%s : Median Latency time at %x req/s was %y ms"
            },
            colors: ["#9ACD32", "#FF6347"]
        };
    },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesLatencyVsRequest"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotLatenciesVsRequest"), dataset, options);
        // setup overview
        $.plot($("#overviewLatenciesVsRequest"), dataset, prepareOverviewOptions(options));
    }
};

// Latencies vs Request
function refreshLatenciesVsRequest() {
        var infos = latenciesVsRequestInfos;
        prepareSeries(infos.data);
        if(isGraph($("#flotLatenciesVsRequest"))){
            infos.createGraph();
        }else{
            var choiceContainer = $("#choicesLatencyVsRequest");
            createLegend(choiceContainer, infos);
            infos.createGraph();
            setGraphZoomable("#flotLatenciesVsRequest", "#overviewLatenciesVsRequest");
            $('#footerLatenciesVsRequest .legendColorBox > div').each(function(i){
                $(this).clone().prependTo(choiceContainer.find("li").eq(i));
            });
        }
};

var hitsPerSecondInfos = {
        data: {"result": {"minY": 1.6666666666666667, "minX": 1.65636546E12, "maxY": 1.6666666666666667, "series": [{"data": [[1.65636546E12, 1.6666666666666667]], "isOverall": false, "label": "hitsPerSecond", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.65636546E12, "title": "Hits Per Second"}},
        getOptions: function() {
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of hits / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendHitsPerSecond"
                },
                selection: {
                    mode : 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y.2 hits/sec"
                }
            };
        },
        createGraph: function createGraph() {
            var data = this.data;
            var dataset = prepareData(data.result.series, $("#choicesHitsPerSecond"));
            var options = this.getOptions();
            prepareOptions(options, data);
            $.plot($("#flotHitsPerSecond"), dataset, options);
            // setup overview
            $.plot($("#overviewHitsPerSecond"), dataset, prepareOverviewOptions(options));
        }
};

// Hits per second
function refreshHitsPerSecond(fixTimestamps) {
    var infos = hitsPerSecondInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if (isGraph($("#flotHitsPerSecond"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesHitsPerSecond");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotHitsPerSecond", "#overviewHitsPerSecond");
        $('#footerHitsPerSecond .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
}

var codesPerSecondInfos = {
        data: {"result": {"minY": 1.6666666666666667, "minX": 1.65636546E12, "maxY": 1.6666666666666667, "series": [{"data": [[1.65636546E12, 1.6666666666666667]], "isOverall": false, "label": "200", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.65636546E12, "title": "Codes Per Second"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of responses / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendCodesPerSecond"
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "Number of Response Codes %s at %x was %y.2 responses / sec"
                }
            };
        },
    createGraph: function() {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesCodesPerSecond"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotCodesPerSecond"), dataset, options);
        // setup overview
        $.plot($("#overviewCodesPerSecond"), dataset, prepareOverviewOptions(options));
    }
};

// Codes per second
function refreshCodesPerSecond(fixTimestamps) {
    var infos = codesPerSecondInfos;
    prepareSeries(infos.data);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotCodesPerSecond"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesCodesPerSecond");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotCodesPerSecond", "#overviewCodesPerSecond");
        $('#footerCodesPerSecond .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var transactionsPerSecondInfos = {
        data: {"result": {"minY": 1.6666666666666667, "minX": 1.65636546E12, "maxY": 1.6666666666666667, "series": [{"data": [[1.65636546E12, 1.6666666666666667]], "isOverall": false, "label": "Excecute Login-success", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.65636546E12, "title": "Transactions Per Second"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of transactions / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendTransactionsPerSecond"
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y transactions / sec"
                }
            };
        },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesTransactionsPerSecond"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotTransactionsPerSecond"), dataset, options);
        // setup overview
        $.plot($("#overviewTransactionsPerSecond"), dataset, prepareOverviewOptions(options));
    }
};

// Transactions per second
function refreshTransactionsPerSecond(fixTimestamps) {
    var infos = transactionsPerSecondInfos;
    prepareSeries(infos.data);
    if(infos.data.result.series.length == 0) {
        setEmptyGraph("#bodyTransactionsPerSecond");
        return;
    }
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotTransactionsPerSecond"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesTransactionsPerSecond");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotTransactionsPerSecond", "#overviewTransactionsPerSecond");
        $('#footerTransactionsPerSecond .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

var totalTPSInfos = {
        data: {"result": {"minY": 1.6666666666666667, "minX": 1.65636546E12, "maxY": 1.6666666666666667, "series": [{"data": [[1.65636546E12, 1.6666666666666667]], "isOverall": false, "label": "Transaction-success", "isController": false}, {"data": [], "isOverall": false, "label": "Transaction-failure", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.65636546E12, "title": "Total Transactions Per Second"}},
        getOptions: function(){
            return {
                series: {
                    lines: {
                        show: true
                    },
                    points: {
                        show: true
                    }
                },
                xaxis: {
                    mode: "time",
                    timeformat: getTimeFormat(this.data.result.granularity),
                    axisLabel: getElapsedTimeLabel(this.data.result.granularity),
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20,
                },
                yaxis: {
                    axisLabel: "Number of transactions / sec",
                    axisLabelUseCanvas: true,
                    axisLabelFontSizePixels: 12,
                    axisLabelFontFamily: 'Verdana, Arial',
                    axisLabelPadding: 20
                },
                legend: {
                    noColumns: 2,
                    show: true,
                    container: "#legendTotalTPS"
                },
                selection: {
                    mode: 'xy'
                },
                grid: {
                    hoverable: true // IMPORTANT! this is needed for tooltip to
                                    // work
                },
                tooltip: true,
                tooltipOpts: {
                    content: "%s at %x was %y transactions / sec"
                },
                colors: ["#9ACD32", "#FF6347"]
            };
        },
    createGraph: function () {
        var data = this.data;
        var dataset = prepareData(data.result.series, $("#choicesTotalTPS"));
        var options = this.getOptions();
        prepareOptions(options, data);
        $.plot($("#flotTotalTPS"), dataset, options);
        // setup overview
        $.plot($("#overviewTotalTPS"), dataset, prepareOverviewOptions(options));
    }
};

// Total Transactions per second
function refreshTotalTPS(fixTimestamps) {
    var infos = totalTPSInfos;
    // We want to ignore seriesFilter
    prepareSeries(infos.data, false, true);
    if(fixTimestamps) {
        fixTimeStamps(infos.data.result.series, 10800000);
    }
    if(isGraph($("#flotTotalTPS"))){
        infos.createGraph();
    }else{
        var choiceContainer = $("#choicesTotalTPS");
        createLegend(choiceContainer, infos);
        infos.createGraph();
        setGraphZoomable("#flotTotalTPS", "#overviewTotalTPS");
        $('#footerTotalTPS .legendColorBox > div').each(function(i){
            $(this).clone().prependTo(choiceContainer.find("li").eq(i));
        });
    }
};

// Collapse the graph matching the specified DOM element depending the collapsed
// status
function collapse(elem, collapsed){
    if(collapsed){
        $(elem).parent().find(".fa-chevron-up").removeClass("fa-chevron-up").addClass("fa-chevron-down");
    } else {
        $(elem).parent().find(".fa-chevron-down").removeClass("fa-chevron-down").addClass("fa-chevron-up");
        if (elem.id == "bodyBytesThroughputOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshBytesThroughputOverTime(true);
            }
            document.location.href="#bytesThroughputOverTime";
        } else if (elem.id == "bodyLatenciesOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshLatenciesOverTime(true);
            }
            document.location.href="#latenciesOverTime";
        } else if (elem.id == "bodyCustomGraph") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshCustomGraph(true);
            }
            document.location.href="#responseCustomGraph";
        } else if (elem.id == "bodyConnectTimeOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshConnectTimeOverTime(true);
            }
            document.location.href="#connectTimeOverTime";
        } else if (elem.id == "bodyResponseTimePercentilesOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshResponseTimePercentilesOverTime(true);
            }
            document.location.href="#responseTimePercentilesOverTime";
        } else if (elem.id == "bodyResponseTimeDistribution") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshResponseTimeDistribution();
            }
            document.location.href="#responseTimeDistribution" ;
        } else if (elem.id == "bodySyntheticResponseTimeDistribution") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshSyntheticResponseTimeDistribution();
            }
            document.location.href="#syntheticResponseTimeDistribution" ;
        } else if (elem.id == "bodyActiveThreadsOverTime") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshActiveThreadsOverTime(true);
            }
            document.location.href="#activeThreadsOverTime";
        } else if (elem.id == "bodyTimeVsThreads") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshTimeVsThreads();
            }
            document.location.href="#timeVsThreads" ;
        } else if (elem.id == "bodyCodesPerSecond") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshCodesPerSecond(true);
            }
            document.location.href="#codesPerSecond";
        } else if (elem.id == "bodyTransactionsPerSecond") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshTransactionsPerSecond(true);
            }
            document.location.href="#transactionsPerSecond";
        } else if (elem.id == "bodyTotalTPS") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshTotalTPS(true);
            }
            document.location.href="#totalTPS";
        } else if (elem.id == "bodyResponseTimeVsRequest") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshResponseTimeVsRequest();
            }
            document.location.href="#responseTimeVsRequest";
        } else if (elem.id == "bodyLatenciesVsRequest") {
            if (isGraph($(elem).find('.flot-chart-content')) == false) {
                refreshLatenciesVsRequest();
            }
            document.location.href="#latencyVsRequest";
        }
    }
}

/*
 * Activates or deactivates all series of the specified graph (represented by id parameter)
 * depending on checked argument.
 */
function toggleAll(id, checked){
    var placeholder = document.getElementById(id);

    var cases = $(placeholder).find(':checkbox');
    cases.prop('checked', checked);
    $(cases).parent().children().children().toggleClass("legend-disabled", !checked);

    var choiceContainer;
    if ( id == "choicesBytesThroughputOverTime"){
        choiceContainer = $("#choicesBytesThroughputOverTime");
        refreshBytesThroughputOverTime(false);
    } else if(id == "choicesResponseTimesOverTime"){
        choiceContainer = $("#choicesResponseTimesOverTime");
        refreshResponseTimeOverTime(false);
    }else if(id == "choicesResponseCustomGraph"){
        choiceContainer = $("#choicesResponseCustomGraph");
        refreshCustomGraph(false);
    } else if ( id == "choicesLatenciesOverTime"){
        choiceContainer = $("#choicesLatenciesOverTime");
        refreshLatenciesOverTime(false);
    } else if ( id == "choicesConnectTimeOverTime"){
        choiceContainer = $("#choicesConnectTimeOverTime");
        refreshConnectTimeOverTime(false);
    } else if ( id == "choicesResponseTimePercentilesOverTime"){
        choiceContainer = $("#choicesResponseTimePercentilesOverTime");
        refreshResponseTimePercentilesOverTime(false);
    } else if ( id == "choicesResponseTimePercentiles"){
        choiceContainer = $("#choicesResponseTimePercentiles");
        refreshResponseTimePercentiles();
    } else if(id == "choicesActiveThreadsOverTime"){
        choiceContainer = $("#choicesActiveThreadsOverTime");
        refreshActiveThreadsOverTime(false);
    } else if ( id == "choicesTimeVsThreads"){
        choiceContainer = $("#choicesTimeVsThreads");
        refreshTimeVsThreads();
    } else if ( id == "choicesSyntheticResponseTimeDistribution"){
        choiceContainer = $("#choicesSyntheticResponseTimeDistribution");
        refreshSyntheticResponseTimeDistribution();
    } else if ( id == "choicesResponseTimeDistribution"){
        choiceContainer = $("#choicesResponseTimeDistribution");
        refreshResponseTimeDistribution();
    } else if ( id == "choicesHitsPerSecond"){
        choiceContainer = $("#choicesHitsPerSecond");
        refreshHitsPerSecond(false);
    } else if(id == "choicesCodesPerSecond"){
        choiceContainer = $("#choicesCodesPerSecond");
        refreshCodesPerSecond(false);
    } else if ( id == "choicesTransactionsPerSecond"){
        choiceContainer = $("#choicesTransactionsPerSecond");
        refreshTransactionsPerSecond(false);
    } else if ( id == "choicesTotalTPS"){
        choiceContainer = $("#choicesTotalTPS");
        refreshTotalTPS(false);
    } else if ( id == "choicesResponseTimeVsRequest"){
        choiceContainer = $("#choicesResponseTimeVsRequest");
        refreshResponseTimeVsRequest();
    } else if ( id == "choicesLatencyVsRequest"){
        choiceContainer = $("#choicesLatencyVsRequest");
        refreshLatenciesVsRequest();
    }
    var color = checked ? "black" : "#818181";
    if(choiceContainer != null) {
        choiceContainer.find("label").each(function(){
            this.style.color = color;
        });
    }
}

