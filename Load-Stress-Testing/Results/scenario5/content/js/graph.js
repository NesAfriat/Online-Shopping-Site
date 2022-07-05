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
        data: {"result": {"minY": 5.0, "minX": 0.0, "maxY": 729.0, "series": [{"data": [[0.0, 5.0], [0.1, 5.0], [0.2, 5.0], [0.3, 5.0], [0.4, 5.0], [0.5, 5.0], [0.6, 5.0], [0.7, 5.0], [0.8, 5.0], [0.9, 5.0], [1.0, 5.0], [1.1, 5.0], [1.2, 5.0], [1.3, 5.0], [1.4, 5.0], [1.5, 5.0], [1.6, 5.0], [1.7, 5.0], [1.8, 5.0], [1.9, 5.0], [2.0, 6.0], [2.1, 6.0], [2.2, 6.0], [2.3, 6.0], [2.4, 6.0], [2.5, 6.0], [2.6, 6.0], [2.7, 6.0], [2.8, 6.0], [2.9, 6.0], [3.0, 7.0], [3.1, 7.0], [3.2, 7.0], [3.3, 7.0], [3.4, 7.0], [3.5, 7.0], [3.6, 7.0], [3.7, 7.0], [3.8, 7.0], [3.9, 7.0], [4.0, 8.0], [4.1, 8.0], [4.2, 8.0], [4.3, 8.0], [4.4, 8.0], [4.5, 8.0], [4.6, 8.0], [4.7, 8.0], [4.8, 8.0], [4.9, 8.0], [5.0, 41.0], [5.1, 41.0], [5.2, 41.0], [5.3, 41.0], [5.4, 41.0], [5.5, 41.0], [5.6, 41.0], [5.7, 41.0], [5.8, 41.0], [5.9, 41.0], [6.0, 49.0], [6.1, 49.0], [6.2, 49.0], [6.3, 49.0], [6.4, 49.0], [6.5, 49.0], [6.6, 49.0], [6.7, 49.0], [6.8, 49.0], [6.9, 49.0], [7.0, 67.0], [7.1, 67.0], [7.2, 67.0], [7.3, 67.0], [7.4, 67.0], [7.5, 67.0], [7.6, 67.0], [7.7, 67.0], [7.8, 67.0], [7.9, 67.0], [8.0, 75.0], [8.1, 75.0], [8.2, 75.0], [8.3, 75.0], [8.4, 75.0], [8.5, 75.0], [8.6, 75.0], [8.7, 75.0], [8.8, 75.0], [8.9, 75.0], [9.0, 78.0], [9.1, 78.0], [9.2, 78.0], [9.3, 78.0], [9.4, 78.0], [9.5, 78.0], [9.6, 78.0], [9.7, 78.0], [9.8, 78.0], [9.9, 78.0], [10.0, 87.0], [10.1, 87.0], [10.2, 87.0], [10.3, 87.0], [10.4, 87.0], [10.5, 87.0], [10.6, 87.0], [10.7, 87.0], [10.8, 87.0], [10.9, 87.0], [11.0, 100.0], [11.1, 100.0], [11.2, 100.0], [11.3, 100.0], [11.4, 100.0], [11.5, 100.0], [11.6, 100.0], [11.7, 100.0], [11.8, 100.0], [11.9, 100.0], [12.0, 101.0], [12.1, 101.0], [12.2, 101.0], [12.3, 101.0], [12.4, 101.0], [12.5, 101.0], [12.6, 101.0], [12.7, 101.0], [12.8, 101.0], [12.9, 101.0], [13.0, 109.0], [13.1, 109.0], [13.2, 109.0], [13.3, 109.0], [13.4, 109.0], [13.5, 109.0], [13.6, 109.0], [13.7, 109.0], [13.8, 109.0], [13.9, 109.0], [14.0, 118.0], [14.1, 118.0], [14.2, 118.0], [14.3, 118.0], [14.4, 118.0], [14.5, 118.0], [14.6, 118.0], [14.7, 118.0], [14.8, 118.0], [14.9, 118.0], [15.0, 119.0], [15.1, 119.0], [15.2, 119.0], [15.3, 119.0], [15.4, 119.0], [15.5, 119.0], [15.6, 119.0], [15.7, 119.0], [15.8, 119.0], [15.9, 119.0], [16.0, 124.0], [16.1, 124.0], [16.2, 124.0], [16.3, 124.0], [16.4, 124.0], [16.5, 124.0], [16.6, 124.0], [16.7, 124.0], [16.8, 124.0], [16.9, 124.0], [17.0, 131.0], [17.1, 131.0], [17.2, 131.0], [17.3, 131.0], [17.4, 131.0], [17.5, 131.0], [17.6, 131.0], [17.7, 131.0], [17.8, 131.0], [17.9, 131.0], [18.0, 149.0], [18.1, 149.0], [18.2, 149.0], [18.3, 149.0], [18.4, 149.0], [18.5, 149.0], [18.6, 149.0], [18.7, 149.0], [18.8, 149.0], [18.9, 149.0], [19.0, 152.0], [19.1, 152.0], [19.2, 152.0], [19.3, 152.0], [19.4, 152.0], [19.5, 152.0], [19.6, 152.0], [19.7, 152.0], [19.8, 152.0], [19.9, 152.0], [20.0, 161.0], [20.1, 161.0], [20.2, 161.0], [20.3, 161.0], [20.4, 161.0], [20.5, 161.0], [20.6, 161.0], [20.7, 161.0], [20.8, 161.0], [20.9, 161.0], [21.0, 167.0], [21.1, 167.0], [21.2, 167.0], [21.3, 167.0], [21.4, 167.0], [21.5, 167.0], [21.6, 167.0], [21.7, 167.0], [21.8, 167.0], [21.9, 167.0], [22.0, 170.0], [22.1, 170.0], [22.2, 170.0], [22.3, 170.0], [22.4, 170.0], [22.5, 170.0], [22.6, 170.0], [22.7, 170.0], [22.8, 170.0], [22.9, 170.0], [23.0, 172.0], [23.1, 172.0], [23.2, 172.0], [23.3, 172.0], [23.4, 172.0], [23.5, 172.0], [23.6, 172.0], [23.7, 172.0], [23.8, 172.0], [23.9, 172.0], [24.0, 177.0], [24.1, 177.0], [24.2, 177.0], [24.3, 177.0], [24.4, 177.0], [24.5, 177.0], [24.6, 177.0], [24.7, 177.0], [24.8, 177.0], [24.9, 177.0], [25.0, 196.0], [25.1, 196.0], [25.2, 196.0], [25.3, 196.0], [25.4, 196.0], [25.5, 196.0], [25.6, 196.0], [25.7, 196.0], [25.8, 196.0], [25.9, 196.0], [26.0, 217.0], [26.1, 217.0], [26.2, 217.0], [26.3, 217.0], [26.4, 217.0], [26.5, 217.0], [26.6, 217.0], [26.7, 217.0], [26.8, 217.0], [26.9, 217.0], [27.0, 219.0], [27.1, 219.0], [27.2, 219.0], [27.3, 219.0], [27.4, 219.0], [27.5, 219.0], [27.6, 219.0], [27.7, 219.0], [27.8, 219.0], [27.9, 219.0], [28.0, 231.0], [28.1, 231.0], [28.2, 231.0], [28.3, 231.0], [28.4, 231.0], [28.5, 231.0], [28.6, 231.0], [28.7, 231.0], [28.8, 231.0], [28.9, 231.0], [29.0, 235.0], [29.1, 235.0], [29.2, 235.0], [29.3, 235.0], [29.4, 235.0], [29.5, 235.0], [29.6, 235.0], [29.7, 235.0], [29.8, 235.0], [29.9, 235.0], [30.0, 241.0], [30.1, 241.0], [30.2, 241.0], [30.3, 241.0], [30.4, 241.0], [30.5, 241.0], [30.6, 241.0], [30.7, 241.0], [30.8, 241.0], [30.9, 241.0], [31.0, 244.0], [31.1, 244.0], [31.2, 244.0], [31.3, 244.0], [31.4, 244.0], [31.5, 244.0], [31.6, 244.0], [31.7, 244.0], [31.8, 244.0], [31.9, 244.0], [32.0, 245.0], [32.1, 245.0], [32.2, 245.0], [32.3, 245.0], [32.4, 245.0], [32.5, 245.0], [32.6, 245.0], [32.7, 245.0], [32.8, 245.0], [32.9, 245.0], [33.0, 264.0], [33.1, 264.0], [33.2, 264.0], [33.3, 264.0], [33.4, 264.0], [33.5, 264.0], [33.6, 264.0], [33.7, 264.0], [33.8, 264.0], [33.9, 264.0], [34.0, 265.0], [34.1, 265.0], [34.2, 265.0], [34.3, 265.0], [34.4, 265.0], [34.5, 265.0], [34.6, 265.0], [34.7, 265.0], [34.8, 265.0], [34.9, 265.0], [35.0, 269.0], [35.1, 269.0], [35.2, 269.0], [35.3, 269.0], [35.4, 269.0], [35.5, 269.0], [35.6, 269.0], [35.7, 269.0], [35.8, 269.0], [35.9, 269.0], [36.0, 270.0], [36.1, 270.0], [36.2, 270.0], [36.3, 270.0], [36.4, 270.0], [36.5, 270.0], [36.6, 270.0], [36.7, 270.0], [36.8, 270.0], [36.9, 270.0], [37.0, 271.0], [37.1, 271.0], [37.2, 271.0], [37.3, 271.0], [37.4, 271.0], [37.5, 271.0], [37.6, 271.0], [37.7, 271.0], [37.8, 271.0], [37.9, 271.0], [38.0, 273.0], [38.1, 273.0], [38.2, 273.0], [38.3, 273.0], [38.4, 273.0], [38.5, 273.0], [38.6, 273.0], [38.7, 273.0], [38.8, 273.0], [38.9, 273.0], [39.0, 284.0], [39.1, 284.0], [39.2, 284.0], [39.3, 284.0], [39.4, 284.0], [39.5, 284.0], [39.6, 284.0], [39.7, 284.0], [39.8, 284.0], [39.9, 284.0], [40.0, 290.0], [40.1, 290.0], [40.2, 290.0], [40.3, 290.0], [40.4, 290.0], [40.5, 290.0], [40.6, 290.0], [40.7, 290.0], [40.8, 290.0], [40.9, 290.0], [41.0, 293.0], [41.1, 293.0], [41.2, 293.0], [41.3, 293.0], [41.4, 293.0], [41.5, 293.0], [41.6, 293.0], [41.7, 293.0], [41.8, 293.0], [41.9, 293.0], [42.0, 297.0], [42.1, 297.0], [42.2, 297.0], [42.3, 297.0], [42.4, 297.0], [42.5, 297.0], [42.6, 297.0], [42.7, 297.0], [42.8, 297.0], [42.9, 297.0], [43.0, 304.0], [43.1, 304.0], [43.2, 304.0], [43.3, 304.0], [43.4, 304.0], [43.5, 304.0], [43.6, 304.0], [43.7, 304.0], [43.8, 304.0], [43.9, 304.0], [44.0, 306.0], [44.1, 306.0], [44.2, 306.0], [44.3, 306.0], [44.4, 306.0], [44.5, 306.0], [44.6, 306.0], [44.7, 306.0], [44.8, 306.0], [44.9, 306.0], [45.0, 311.0], [45.1, 311.0], [45.2, 311.0], [45.3, 311.0], [45.4, 311.0], [45.5, 311.0], [45.6, 311.0], [45.7, 311.0], [45.8, 311.0], [45.9, 311.0], [46.0, 319.0], [46.1, 319.0], [46.2, 319.0], [46.3, 319.0], [46.4, 319.0], [46.5, 319.0], [46.6, 319.0], [46.7, 319.0], [46.8, 319.0], [46.9, 319.0], [47.0, 321.0], [47.1, 321.0], [47.2, 321.0], [47.3, 321.0], [47.4, 321.0], [47.5, 321.0], [47.6, 321.0], [47.7, 321.0], [47.8, 321.0], [47.9, 321.0], [48.0, 340.0], [48.1, 340.0], [48.2, 340.0], [48.3, 340.0], [48.4, 340.0], [48.5, 340.0], [48.6, 340.0], [48.7, 340.0], [48.8, 340.0], [48.9, 340.0], [49.0, 341.0], [49.1, 341.0], [49.2, 341.0], [49.3, 341.0], [49.4, 341.0], [49.5, 341.0], [49.6, 341.0], [49.7, 341.0], [49.8, 341.0], [49.9, 341.0], [50.0, 353.0], [50.1, 353.0], [50.2, 353.0], [50.3, 353.0], [50.4, 353.0], [50.5, 353.0], [50.6, 353.0], [50.7, 353.0], [50.8, 353.0], [50.9, 353.0], [51.0, 368.0], [51.1, 368.0], [51.2, 368.0], [51.3, 368.0], [51.4, 368.0], [51.5, 368.0], [51.6, 368.0], [51.7, 368.0], [51.8, 368.0], [51.9, 368.0], [52.0, 377.0], [52.1, 377.0], [52.2, 377.0], [52.3, 377.0], [52.4, 377.0], [52.5, 377.0], [52.6, 377.0], [52.7, 377.0], [52.8, 377.0], [52.9, 377.0], [53.0, 378.0], [53.1, 378.0], [53.2, 378.0], [53.3, 378.0], [53.4, 378.0], [53.5, 378.0], [53.6, 378.0], [53.7, 378.0], [53.8, 378.0], [53.9, 378.0], [54.0, 388.0], [54.1, 388.0], [54.2, 388.0], [54.3, 388.0], [54.4, 388.0], [54.5, 388.0], [54.6, 388.0], [54.7, 388.0], [54.8, 388.0], [54.9, 388.0], [55.0, 400.0], [55.1, 400.0], [55.2, 400.0], [55.3, 400.0], [55.4, 400.0], [55.5, 400.0], [55.6, 400.0], [55.7, 400.0], [55.8, 400.0], [55.9, 400.0], [56.0, 402.0], [56.1, 402.0], [56.2, 402.0], [56.3, 402.0], [56.4, 402.0], [56.5, 402.0], [56.6, 402.0], [56.7, 402.0], [56.8, 402.0], [56.9, 402.0], [57.0, 406.0], [57.1, 406.0], [57.2, 406.0], [57.3, 406.0], [57.4, 406.0], [57.5, 406.0], [57.6, 406.0], [57.7, 406.0], [57.8, 406.0], [57.9, 406.0], [58.0, 433.0], [58.1, 433.0], [58.2, 433.0], [58.3, 433.0], [58.4, 433.0], [58.5, 433.0], [58.6, 433.0], [58.7, 433.0], [58.8, 433.0], [58.9, 433.0], [59.0, 438.0], [59.1, 438.0], [59.2, 438.0], [59.3, 438.0], [59.4, 438.0], [59.5, 438.0], [59.6, 438.0], [59.7, 438.0], [59.8, 438.0], [59.9, 438.0], [60.0, 449.0], [60.1, 449.0], [60.2, 449.0], [60.3, 449.0], [60.4, 449.0], [60.5, 449.0], [60.6, 449.0], [60.7, 449.0], [60.8, 449.0], [60.9, 449.0], [61.0, 473.0], [61.1, 473.0], [61.2, 473.0], [61.3, 473.0], [61.4, 473.0], [61.5, 473.0], [61.6, 473.0], [61.7, 473.0], [61.8, 473.0], [61.9, 473.0], [62.0, 475.0], [62.1, 475.0], [62.2, 475.0], [62.3, 475.0], [62.4, 475.0], [62.5, 475.0], [62.6, 475.0], [62.7, 475.0], [62.8, 475.0], [62.9, 475.0], [63.0, 479.0], [63.1, 479.0], [63.2, 479.0], [63.3, 479.0], [63.4, 479.0], [63.5, 479.0], [63.6, 479.0], [63.7, 479.0], [63.8, 479.0], [63.9, 479.0], [64.0, 485.0], [64.1, 485.0], [64.2, 485.0], [64.3, 485.0], [64.4, 485.0], [64.5, 485.0], [64.6, 485.0], [64.7, 485.0], [64.8, 485.0], [64.9, 485.0], [65.0, 502.0], [65.1, 502.0], [65.2, 502.0], [65.3, 502.0], [65.4, 502.0], [65.5, 502.0], [65.6, 502.0], [65.7, 502.0], [65.8, 502.0], [65.9, 502.0], [66.0, 508.0], [66.1, 508.0], [66.2, 508.0], [66.3, 508.0], [66.4, 508.0], [66.5, 508.0], [66.6, 508.0], [66.7, 508.0], [66.8, 508.0], [66.9, 508.0], [67.0, 510.0], [67.1, 510.0], [67.2, 510.0], [67.3, 510.0], [67.4, 510.0], [67.5, 510.0], [67.6, 510.0], [67.7, 510.0], [67.8, 510.0], [67.9, 510.0], [68.0, 519.0], [68.1, 519.0], [68.2, 519.0], [68.3, 519.0], [68.4, 519.0], [68.5, 519.0], [68.6, 519.0], [68.7, 519.0], [68.8, 519.0], [68.9, 519.0], [69.0, 539.0], [69.1, 539.0], [69.2, 539.0], [69.3, 539.0], [69.4, 539.0], [69.5, 539.0], [69.6, 539.0], [69.7, 539.0], [69.8, 539.0], [69.9, 539.0], [70.0, 542.0], [70.1, 542.0], [70.2, 542.0], [70.3, 542.0], [70.4, 542.0], [70.5, 542.0], [70.6, 542.0], [70.7, 542.0], [70.8, 542.0], [70.9, 542.0], [71.0, 547.0], [71.1, 547.0], [71.2, 547.0], [71.3, 547.0], [71.4, 547.0], [71.5, 547.0], [71.6, 547.0], [71.7, 547.0], [71.8, 547.0], [71.9, 547.0], [72.0, 553.0], [72.1, 553.0], [72.2, 553.0], [72.3, 553.0], [72.4, 553.0], [72.5, 553.0], [72.6, 553.0], [72.7, 553.0], [72.8, 553.0], [72.9, 553.0], [73.0, 562.0], [73.1, 562.0], [73.2, 562.0], [73.3, 562.0], [73.4, 562.0], [73.5, 562.0], [73.6, 562.0], [73.7, 562.0], [73.8, 562.0], [73.9, 562.0], [74.0, 569.0], [74.1, 569.0], [74.2, 569.0], [74.3, 569.0], [74.4, 569.0], [74.5, 569.0], [74.6, 569.0], [74.7, 569.0], [74.8, 569.0], [74.9, 569.0], [75.0, 575.0], [75.1, 575.0], [75.2, 575.0], [75.3, 575.0], [75.4, 575.0], [75.5, 575.0], [75.6, 575.0], [75.7, 575.0], [75.8, 575.0], [75.9, 575.0], [76.0, 577.0], [76.1, 577.0], [76.2, 577.0], [76.3, 577.0], [76.4, 577.0], [76.5, 577.0], [76.6, 577.0], [76.7, 577.0], [76.8, 577.0], [76.9, 577.0], [77.0, 584.0], [77.1, 584.0], [77.2, 584.0], [77.3, 584.0], [77.4, 584.0], [77.5, 584.0], [77.6, 584.0], [77.7, 584.0], [77.8, 584.0], [77.9, 584.0], [78.0, 596.0], [78.1, 596.0], [78.2, 596.0], [78.3, 596.0], [78.4, 596.0], [78.5, 596.0], [78.6, 596.0], [78.7, 596.0], [78.8, 596.0], [78.9, 596.0], [79.0, 602.0], [79.1, 602.0], [79.2, 602.0], [79.3, 602.0], [79.4, 602.0], [79.5, 602.0], [79.6, 602.0], [79.7, 602.0], [79.8, 602.0], [79.9, 602.0], [80.0, 605.0], [80.1, 605.0], [80.2, 605.0], [80.3, 605.0], [80.4, 605.0], [80.5, 605.0], [80.6, 605.0], [80.7, 605.0], [80.8, 605.0], [80.9, 605.0], [81.0, 620.0], [81.1, 620.0], [81.2, 620.0], [81.3, 620.0], [81.4, 620.0], [81.5, 620.0], [81.6, 620.0], [81.7, 620.0], [81.8, 620.0], [81.9, 620.0], [82.0, 654.0], [82.1, 654.0], [82.2, 654.0], [82.3, 654.0], [82.4, 654.0], [82.5, 654.0], [82.6, 654.0], [82.7, 654.0], [82.8, 654.0], [82.9, 654.0], [83.0, 660.0], [83.1, 660.0], [83.2, 660.0], [83.3, 660.0], [83.4, 660.0], [83.5, 660.0], [83.6, 660.0], [83.7, 660.0], [83.8, 660.0], [83.9, 660.0], [84.0, 673.0], [84.1, 673.0], [84.2, 673.0], [84.3, 673.0], [84.4, 673.0], [84.5, 673.0], [84.6, 673.0], [84.7, 673.0], [84.8, 673.0], [84.9, 673.0], [85.0, 674.0], [85.1, 674.0], [85.2, 674.0], [85.3, 674.0], [85.4, 674.0], [85.5, 674.0], [85.6, 674.0], [85.7, 674.0], [85.8, 674.0], [85.9, 674.0], [86.0, 686.0], [86.1, 686.0], [86.2, 686.0], [86.3, 686.0], [86.4, 686.0], [86.5, 686.0], [86.6, 686.0], [86.7, 686.0], [86.8, 686.0], [86.9, 686.0], [87.0, 688.0], [87.1, 688.0], [87.2, 688.0], [87.3, 688.0], [87.4, 688.0], [87.5, 688.0], [87.6, 688.0], [87.7, 688.0], [87.8, 688.0], [87.9, 688.0], [88.0, 694.0], [88.1, 694.0], [88.2, 694.0], [88.3, 694.0], [88.4, 694.0], [88.5, 694.0], [88.6, 694.0], [88.7, 694.0], [88.8, 694.0], [88.9, 694.0], [89.0, 694.0], [89.1, 694.0], [89.2, 694.0], [89.3, 694.0], [89.4, 694.0], [89.5, 694.0], [89.6, 694.0], [89.7, 694.0], [89.8, 694.0], [89.9, 694.0], [90.0, 703.0], [90.1, 703.0], [90.2, 703.0], [90.3, 703.0], [90.4, 703.0], [90.5, 703.0], [90.6, 703.0], [90.7, 703.0], [90.8, 703.0], [90.9, 703.0], [91.0, 703.0], [91.1, 703.0], [91.2, 703.0], [91.3, 703.0], [91.4, 703.0], [91.5, 703.0], [91.6, 703.0], [91.7, 703.0], [91.8, 703.0], [91.9, 703.0], [92.0, 709.0], [92.1, 709.0], [92.2, 709.0], [92.3, 709.0], [92.4, 709.0], [92.5, 709.0], [92.6, 709.0], [92.7, 709.0], [92.8, 709.0], [92.9, 709.0], [93.0, 710.0], [93.1, 710.0], [93.2, 710.0], [93.3, 710.0], [93.4, 710.0], [93.5, 710.0], [93.6, 710.0], [93.7, 710.0], [93.8, 710.0], [93.9, 710.0], [94.0, 719.0], [94.1, 719.0], [94.2, 719.0], [94.3, 719.0], [94.4, 719.0], [94.5, 719.0], [94.6, 719.0], [94.7, 719.0], [94.8, 719.0], [94.9, 719.0], [95.0, 719.0], [95.1, 719.0], [95.2, 719.0], [95.3, 719.0], [95.4, 719.0], [95.5, 719.0], [95.6, 719.0], [95.7, 719.0], [95.8, 719.0], [95.9, 719.0], [96.0, 721.0], [96.1, 721.0], [96.2, 721.0], [96.3, 721.0], [96.4, 721.0], [96.5, 721.0], [96.6, 721.0], [96.7, 721.0], [96.8, 721.0], [96.9, 721.0], [97.0, 721.0], [97.1, 721.0], [97.2, 721.0], [97.3, 721.0], [97.4, 721.0], [97.5, 721.0], [97.6, 721.0], [97.7, 721.0], [97.8, 721.0], [97.9, 721.0], [98.0, 728.0], [98.1, 728.0], [98.2, 728.0], [98.3, 728.0], [98.4, 728.0], [98.5, 728.0], [98.6, 728.0], [98.7, 728.0], [98.8, 728.0], [98.9, 728.0], [99.0, 729.0], [99.1, 729.0], [99.2, 729.0], [99.3, 729.0], [99.4, 729.0], [99.5, 729.0], [99.6, 729.0], [99.7, 729.0], [99.8, 729.0], [99.9, 729.0]], "isOverall": false, "label": "Excecute open shop", "isController": false}], "supportsControllersDiscrimination": true, "maxX": 100.0, "title": "Response Time Percentiles"}},
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
        data: {"result": {"minY": 10.0, "minX": 0.0, "maxY": 17.0, "series": [{"data": [[0.0, 11.0], [300.0, 12.0], [600.0, 11.0], [700.0, 10.0], [400.0, 10.0], [100.0, 15.0], [200.0, 17.0], [500.0, 14.0]], "isOverall": false, "label": "Excecute open shop", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 100, "maxX": 700.0, "title": "Response Time Distribution"}},
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
        data: {"result": {"minY": 35.0, "minX": 0.0, "ticks": [[0, "Requests having \nresponse time <= 500ms"], [1, "Requests having \nresponse time > 500ms and <= 1,500ms"], [2, "Requests having \nresponse time > 1,500ms"], [3, "Requests in error"]], "maxY": 65.0, "series": [{"data": [[0.0, 65.0]], "color": "#9ACD32", "isOverall": false, "label": "Requests having \nresponse time <= 500ms", "isController": false}, {"data": [[1.0, 35.0]], "color": "yellow", "isOverall": false, "label": "Requests having \nresponse time > 500ms and <= 1,500ms", "isController": false}, {"data": [], "color": "orange", "isOverall": false, "label": "Requests having \nresponse time > 1,500ms", "isController": false}, {"data": [], "color": "#FF6347", "isOverall": false, "label": "Requests in error", "isController": false}], "supportsControllersDiscrimination": false, "maxX": 1.0, "title": "Synthetic Response Times Distribution"}},
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
        data: {"result": {"minY": 38.529999999999994, "minX": 1.6563696E12, "maxY": 38.529999999999994, "series": [{"data": [[1.6563696E12, 38.529999999999994]], "isOverall": false, "label": "100-Open Shops", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.6563696E12, "title": "Active Threads Over Time"}},
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
        data: {"result": {"minY": 75.0, "minX": 1.0, "maxY": 673.0, "series": [{"data": [[32.0, 368.0], [33.0, 131.0], [35.0, 585.3333333333334], [36.0, 75.0], [39.0, 101.0], [43.0, 324.5], [44.0, 533.0], [47.0, 635.6666666666666], [46.0, 567.6666666666666], [49.0, 179.5], [48.0, 596.0], [3.0, 290.5], [50.0, 473.0], [51.0, 152.0], [53.0, 356.0], [57.0, 433.0], [56.0, 577.0], [60.0, 358.3333333333333], [61.0, 363.5], [62.0, 167.0], [67.0, 509.6666666666667], [64.0, 459.5], [4.0, 319.0], [70.0, 519.0], [69.0, 241.0], [75.0, 343.33333333333337], [74.0, 149.0], [72.0, 673.0], [80.0, 212.4], [5.0, 284.0], [6.0, 306.0], [7.0, 264.0], [9.0, 244.5], [10.0, 526.0], [11.0, 77.5], [12.0, 231.42857142857144], [13.0, 427.5], [15.0, 586.5], [1.0, 293.0], [17.0, 271.0], [20.0, 609.25], [23.0, 515.6666666666666], [24.0, 553.0], [26.0, 420.0], [30.0, 366.5], [31.0, 259.3333333333333]], "isOverall": false, "label": "Excecute open shop", "isController": false}, {"data": [[38.529999999999994, 376.23]], "isOverall": false, "label": "Excecute open shop-Aggregated", "isController": false}], "supportsControllersDiscrimination": true, "maxX": 80.0, "title": "Time VS Threads"}},
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
        data : {"result": {"minY": 572.9333333333333, "minX": 1.6563696E12, "maxY": 703.1333333333333, "series": [{"data": [[1.6563696E12, 703.1333333333333]], "isOverall": false, "label": "Bytes received per second", "isController": false}, {"data": [[1.6563696E12, 572.9333333333333]], "isOverall": false, "label": "Bytes sent per second", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.6563696E12, "title": "Bytes Throughput Over Time"}},
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
        data: {"result": {"minY": 376.23, "minX": 1.6563696E12, "maxY": 376.23, "series": [{"data": [[1.6563696E12, 376.23]], "isOverall": false, "label": "Excecute open shop", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.6563696E12, "title": "Response Time Over Time"}},
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
        data: {"result": {"minY": 375.88999999999993, "minX": 1.6563696E12, "maxY": 375.88999999999993, "series": [{"data": [[1.6563696E12, 375.88999999999993]], "isOverall": false, "label": "Excecute open shop", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.6563696E12, "title": "Latencies Over Time"}},
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
        data: {"result": {"minY": 7.520000000000001, "minX": 1.6563696E12, "maxY": 7.520000000000001, "series": [{"data": [[1.6563696E12, 7.520000000000001]], "isOverall": false, "label": "Excecute open shop", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.6563696E12, "title": "Connect Time Over Time"}},
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
        data: {"result": {"minY": 5.0, "minX": 1.6563696E12, "maxY": 729.0, "series": [{"data": [[1.6563696E12, 729.0]], "isOverall": false, "label": "Max", "isController": false}, {"data": [[1.6563696E12, 702.1]], "isOverall": false, "label": "90th percentile", "isController": false}, {"data": [[1.6563696E12, 728.99]], "isOverall": false, "label": "99th percentile", "isController": false}, {"data": [[1.6563696E12, 719.0]], "isOverall": false, "label": "95th percentile", "isController": false}, {"data": [[1.6563696E12, 5.0]], "isOverall": false, "label": "Min", "isController": false}, {"data": [[1.6563696E12, 347.0]], "isOverall": false, "label": "Median", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.6563696E12, "title": "Response Time Percentiles Over Time (successful requests only)"}},
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
    data: {"result": {"minY": 302.0, "minX": 4.0, "maxY": 372.5, "series": [{"data": [[4.0, 302.0], [96.0, 372.5]], "isOverall": false, "label": "Successes", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 1000, "maxX": 96.0, "title": "Response Time Vs Request"}},
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
    data: {"result": {"minY": 302.0, "minX": 4.0, "maxY": 372.5, "series": [{"data": [[4.0, 302.0], [96.0, 372.5]], "isOverall": false, "label": "Successes", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 1000, "maxX": 96.0, "title": "Latencies Vs Request"}},
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
        data: {"result": {"minY": 1.6666666666666667, "minX": 1.6563696E12, "maxY": 1.6666666666666667, "series": [{"data": [[1.6563696E12, 1.6666666666666667]], "isOverall": false, "label": "hitsPerSecond", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.6563696E12, "title": "Hits Per Second"}},
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
        data: {"result": {"minY": 1.6666666666666667, "minX": 1.6563696E12, "maxY": 1.6666666666666667, "series": [{"data": [[1.6563696E12, 1.6666666666666667]], "isOverall": false, "label": "200", "isController": false}], "supportsControllersDiscrimination": false, "granularity": 60000, "maxX": 1.6563696E12, "title": "Codes Per Second"}},
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
        data: {"result": {"minY": 1.6666666666666667, "minX": 1.6563696E12, "maxY": 1.6666666666666667, "series": [{"data": [[1.6563696E12, 1.6666666666666667]], "isOverall": false, "label": "Excecute open shop-success", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.6563696E12, "title": "Transactions Per Second"}},
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
        data: {"result": {"minY": 1.6666666666666667, "minX": 1.6563696E12, "maxY": 1.6666666666666667, "series": [{"data": [[1.6563696E12, 1.6666666666666667]], "isOverall": false, "label": "Transaction-success", "isController": false}, {"data": [], "isOverall": false, "label": "Transaction-failure", "isController": false}], "supportsControllersDiscrimination": true, "granularity": 60000, "maxX": 1.6563696E12, "title": "Total Transactions Per Second"}},
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

