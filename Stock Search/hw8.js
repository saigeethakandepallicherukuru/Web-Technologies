// HTML5 validation message for mandatory fields
document.addEventListener("DOMContentLoaded", function() {
    var elements = document.getElementsByTagName("INPUT");
    for (var i = 0; i < elements.length; i++) {
        elements[i].oninvalid = function(e) {
            e.target.setCustomValidity("");
            if (!e.target.validity.valid) {
                e.target.setCustomValidity("Please enter Name or Symbol");
            }
        };
        elements[i].oninput = function(e) {
            e.target.setCustomValidity("");
        };
    }
})

// Reset input field values
function resetValues() {
    document.getElementById("symbol").value="";
    document.getElementById("invalid").innerHTML="";
    $("#gotoStockDetails").addClass("disabled");
}

//To display favourite list
function loadFavouriteList(){
    $("#favoriteItems").empty();
    $("#favoriteItems").append("<tr><th>Symbol</th><th>Company Name</th><th>Stock Price</th><th>Change (Change Percent)</th><th>Market Cap</th><th></th></tr>");
    var k=localStorage.length-1;
    for(var i=0;i<localStorage.length;i++){
        //var symbol=localStorage.getItem(localStorage.key(i));
        var symbol=localStorage.getItem(localStorage.key(k));
      //  alert(localStorage.getItem(localStorage.key(k)));
        k--;
        $.ajax({
          url: "http://stockdetails-1274.appspot.com/",
          cache: false,
          data: {
            CompanySymbol: symbol,
            getQuote: true
          },
          dataType: "json",
          type: 'GET',
          success: function( data ) {
            var data_set = data;
            var row=$("<tr />");
            var change_percent_value="";
            var market_cap_value="";
            $("#favoriteItems").append(row);
            row.append($("<td><button type='button' class='btn btn-link linkStockDetails'>"+data_set["Symbol"]+"</button></td>"));
            row.append($("<td>"+data_set["Name"]+"</td>"));
            row.append($("<td>$ "+data_set["LastPrice"]+"</td>"));
            change_percent_value+=Math.round(data_set["Change"] * 100)/100+" ";
            if((Math.round(data_set["ChangePercent"] * 100)/100) > 0) {
                change_percent_value+="( "+Math.round(data_set["ChangePercent"] * 100)/100+"% )";
                row.append($("<td style='color: green'>"+change_percent_value+"<img src='http://cs-server.usc.edu:45678/hw/hw8/images/up.png' width='35' height='35'></td>"));
            } else {
                change_percent_value+="( "+Math.round(data_set["ChangePercent"] * 100)/100+"% )";
                row.append($("<td style='color: red'>"+change_percent_value+"<img src='http://cs-server.usc.edu:45678/hw/hw8/images/down.png' width='35' height='35'></td>"));
            }
            if((data_set["MarketCap"]/1000000000) < 0.01) {
                        market_cap_value+=Math.round(((data_set["MarketCap"]/1000000000)*1000)*100)/100+" Million</td></tr>";
            } else {
                        market_cap_value+=Math.round((data_set["MarketCap"]/1000000000)*100)/100+" Billion</td></tr>";
            }
            row.append($("<td>"+market_cap_value+"</td>"));  
            row.append($("<td><button type='button' class='btn btn-default removeButton'><span class='glyphicon glyphicon-trash' aria-hidden='true'></span></button></td>"));
            
              //Remove items from favourite list
            $(".removeButton").on("click", function(){
                //var MyRows = $('table#favoriteItems').find('tr');
                var sym=$($(this).closest("tr").find('td:eq(0)')).text();
                localStorage.removeItem(sym);
                $(this).parent().parent().remove();
            })
            
            $(".linkStockDetails").on("click", function(){
                var csym=$($(this).closest("tr").find('td:eq(0)')).text();
                company_Symbol=csym;
                if($(".addFavouriteButton").hasClass('glyphicon-star')) {
                    $(".addFavouriteButton").removeClass('glyphicon-star').addClass('glyphicon-star-empty');   
                }
              //  alert($(this).closest("tr").find('td:eq(0)').text());
                for(var i=0;i<localStorage.length;i++){
                var s=localStorage.getItem(localStorage.key(i));
                if(s==company_Symbol) {
                    if($(".addFavouriteButton").hasClass('glyphicon-star-empty')){
                      //   alert(s);
                        $(".addFavouriteButton").removeClass('glyphicon-star-empty').addClass('glyphicon-star');
                        $(".addFavouriteButton").css("color", "#FFF000");
                    }
                }
            }
    $('#myCarousel').carousel(1);
                $.ajax({
                  url: "http://stockdetails-1274.appspot.com/",
                  cache: false,
                  data: {
                    CompanySymbol: company_Symbol,
                    getQuote: true
                  },
                  dataType: "json",
                  type: 'GET',
                  success: function( data ) {
                      html_text="";
                      last_price=Math.round(data.LastPrice * 100)/100;
                      company_name=data.Name;
                      current_stock_img="http://chart.finance.yahoo.com/t?s="+data.Symbol;
                      change=Math.round(data.Change * 100)/100;
                      change_percent=Math.round(data.ChangePercent * 100)/100;
                      html_text+="<div class='col-md-11' id='Searchresult'><div class='container'><ul class='nav nav-pills'><li class='active'><a href='#CurrentStock' data-toggle='tab'><span class='glyphicon glyphicon-dashboard' aria-hidden='true'></span> Current Stock</a></li><li><a href='#HistoricalCharts' data-toggle='tab'><span class='glyphicon glyphicon-stats' aria-hidden='true'></span> Historical Charts</a></li><li><a href='#NewsFeeds' data-toggle='tab'><span class='glyphicon glyphicon-link' aria-hidden='true'></span> News Feeds</a></li></ul><hr /><div class='tab-content'><div class='tab-pane active' id='CurrentStock'>";
                    html_text+="<div class='container row'><div class='col-md-5'><p><b>Stock Details</b></p><div class='table-responsive'><table class='table table-striped' id='myTable1'><tbody><tr><td><b>Name</b></td><td>"+data.Name+"</td></tr>";
                      html_text+="<tr><td><b>Symbol</b></td><td>"+data.Symbol+"</td></tr>";
                      html_text+="<tr><td><b>Last Price</b></td><td>$ "+Math.round(data.LastPrice * 100)/100+"</td></tr>";
                      html_text+="<tr><td><b>Change (Change Percent)</b></td>";
                      if((Math.round(data.ChangePercent * 100)/100) > 0) {
                        html_text+="<td style='color: green'>"+Math.round(data.Change * 100)/100+" ( "+Math.round(data.ChangePercent * 100)/100+"% )<img src='http://cs-server.usc.edu:45678/hw/hw8/images/up.png' width='20' height='20'></td></tr>";   
                      } else {
                        html_text+="<td style='color: red'>"+Math.round(data.Change * 100)/100+" ( "+Math.round(data.ChangePercent * 100)/100+"% )<img src='http://cs-server.usc.edu:45678/hw/hw8/images/down.png' width='20' height='20'></td></tr>";
                      }
                      var myDate=new Date(data.Timestamp);
                      var monthNames = ["January", "February", "March", "April", "May", "June","July", "August", "September",    "October","November", "December"];
                      html_text+="<tr><td><b>Time and Date</b></td><td>"+ myDate.getDate() + " " + monthNames[myDate.getMonth()] +" "+ myDate.getFullYear()+","+zeroFill(myDate.getHours()) + ":" + zeroFill(myDate.getMinutes()) + ":" + zeroFill(myDate.getSeconds())+(myDate.getHours>=12?' AM':' PM')+"</td></tr>";
                      html_text+="<tr><td><b>Market Cap</b></td>";
                      if((data.MarketCap/1000000000) < 0.01) {
                        html_text+="<td>"+Math.round(((data.MarketCap/1000000000)*1000)*100)/100+" Million</td></tr>";
                      } else {
                        html_text+="<td>"+Math.round((data.MarketCap/1000000000)*100)/100+" Billion</td></tr>";
                      }
                      html_text+="<tr><td><b>Volume</b></td><td>"+data.Volume+"</td></tr>";
                      html_text+="<tr><td><b>Change YTD (Change Percent YTD)</b></td>";
                      if((Math.round(data.ChangePercentYTD * 100)/100) > 0) {
                        html_text+="<td style='color: green'>"+Math.round(data.ChangeYTD * 100)/100+" ( "+Math.round(data.ChangePercentYTD * 100)/100+"% )<img src='http://cs-server.usc.edu:45678/hw/hw8/images/up.png' width='20' height='20'></td></tr>";      
                      } else {
                        html_text+="<td style='color: red'>"+Math.round(data.ChangeYTD * 100)/100+" ( "+Math.round(data.ChangePercentYTD * 100)/100+"% )<img src='http://cs-server.usc.edu:45678/hw/hw8/images/down.png' width='20' height='20'></td></tr>";
                      }
                      html_text+="<tr><td><b>High Price</b></td><td>$ "+Math.round(data.High * 100)/100+"</td></tr>";
                      html_text+="<tr><td><b>Low Price</b></td><td>$ "+Math.round(data.Low * 100)/100+"</td></tr>";
                      html_text+="<tr><td><b>Opening Price</b></td><td>$ "+Math.round(data.Open * 100)/100+"</td></tr>";
                      html_text+="</tbody></table></div></div><div class='col-md-7'><div class='row'><div class='col-md-4 col-md-offset-7'><a href='javascript:fbPost();'><img id=shareButton class='img-responsive' style='display: inline-block' src='fb_icon.png' width='35em' height='35em'></a>&nbsp;&nbsp;<a href='javascript:addFavouriteItem();'><button type='button' class='btn btn-default'><span class='glyphicon glyphicon-star-empty addFavouriteButton'></span></button></a></div></div><img class='img-responsive' src='http://chart.finance.yahoo.com/t?s="+data.Symbol+"&lang=en-US&width=480&height=380'>";
                      html_text+="</div></div></div>";
                      html_text+="<div class='tab-pane' id='HistoricalCharts'><div class='container row'><div class='col-md-12'><div id='container' style='height: 60%; width: 65%;'></div></div></div></div>";
                      generateHistoricalChart();
                      html_text+="<div class='tab-pane' id='NewsFeeds'><div class='container row'><div class='col-md-12'><div id='newsfeed' style='height: 50%;'></div></div></div>";
                      
                      $.get("http://stockdetails-1274.appspot.com/?CompanyNews="+company_Symbol, function(data){
                         if(data) {
                             console.log("News Feed");
                             var data_list = JSON.parse(data);
                             for (var x = 0; x < data_list.d.results.length; x++) {
                                var str = data_list.d.results[x].Description; 
                                var re = new RegExp(company_Symbol,"g");
                                var res = str.replace(re, "<b>"+company_Symbol+"</b>");
                                 var newsDate=new Date(data_list.d.results[x].Date);
                                 $('#newsfeed').append("<div style='background-color: #F0F0F0;border-radius: 0.25em;'><div style='padding: 1em 2em 1em 1em;'><a href='"+data_list.d.results[x].Url+"'>"+data_list.d.results[x].Title+"</a><br /><br /><div style='width: 80%'>"+res+"</div><br />Publisher: "+data_list.d.results[x].Source+"<br /><br />Date: "+newsDate.getDate() + " " + monthNames[newsDate.getMonth()] +" "+ newsDate.getFullYear()+" "+zeroFill(newsDate.getHours())+ ":" + zeroFill(newsDate.getMinutes()) + ":" + zeroFill(newsDate.getSeconds())+"</div></div><br />");
                             }
                             
                         } 
                      });   
                       html_text+="</div></div></div></div>";
                      $('#stockDetails').html(html_text);
                  }
                });
            })
          },
            async: false
        });
    }
    
}

// To post stock details on facebook
function fbPost()
{
    var name="Current Stock Price of "+company_name+" is "+"$"+last_price;
    var description="";
    description+="Stock Information of "+company_name+" ("+company_Symbol+")";
    FB.ui(
    {
        method: 'feed',
        picture: current_stock_img,
        name:name,
        description:description,
        caption:"LAST TRADE PRICE: $ "+last_price+", CHANGE: "+change+" ("+change_percent+"%)",
        link: 'http://dev.markitondemand.com/MODApis/',
    }, 
    function(response)
    {
        if(response && response.post_id) {
            alert("Posted Successfully");
       } else {
           alert("Not Posted");
       }
    }); 
}

window.fbAsyncInit = function() {
    FB.init({
      appId      : '1075010432562872',
      xfbml      : true,
      version    : 'v2.5'
    });
  };

  (function(d, s, id){
     var js, fjs = d.getElementsByTagName(s)[0];
     if (d.getElementById(id)) {return;}
     js = d.createElement(s); js.id = id;
     js.src = "//connect.facebook.net/en_US/sdk.js";
     fjs.parentNode.insertBefore(js, fjs);
   }(document, 'script', 'facebook-jssdk'));


function zeroFill(i) {
  return (i < 10 ? '0' : '') + i
}

function updateValues() {
    var table1 = document.getElementById('favoriteItems');
    var j=0;
    var k=localStorage.length-1;
    for(var i=0;i<localStorage.length;i++) {
     //   var symbol=localStorage.getItem(localStorage.key(i));
        var symbol=localStorage.getItem(localStorage.key(k));
        k--;
      //  alert("update "+symbol);
        $.ajax({
          url: "http://stockdetails-1274.appspot.com/",
          cache: false,
          data: {
            CompanySymbol: symbol,
            getQuote: true
          },
          dataType: "json",
          type: 'GET',
          success: function( data ) {
            var data_items = data;
            stock_price="$ "+data_items["LastPrice"];
            changeValue=Math.round(data_items["Change"] * 100)/100;
            if((Math.round(data_items["ChangePercent"] * 100)/100) > 0) {
                changeValue+=" ( "+Math.round(data_items["ChangePercent"] * 100)/100+"% )"+"<img src='http://cs-server.usc.edu:45678/hw/hw8/images/up.png' width='35' height='35'>";
            } else {
                changeValue+=" ( "+Math.round(data_items["ChangePercent"] * 100)/100+"% )"+"<img src='http://cs-server.usc.edu:45678/hw/hw8/images/down.png' width='35' height='35'>";
            }
            table1.rows[j+1].cells[2].innerHTML=stock_price;
            table1.rows[j+1].cells[3].innerHTML=changeValue;
            j++;
          },
            async: false
        });
    }
}

//To add stock details to favourite list
function addFavouriteItem() {
    if($(".addFavouriteButton").hasClass('glyphicon-star-empty')){
        $(".addFavouriteButton").removeClass('glyphicon-star-empty').addClass('glyphicon-star');
        $(".addFavouriteButton").css("color", "#FFF000");
    }else{
        $(".addFavouriteButton").removeClass('glyphicon-star').addClass('glyphicon-star-empty');  
        $(".addFavouriteButton").css("color", "white");
        localStorage.removeItem(company_Symbol);
        loadFavouriteList();
    }
    localStorage.setItem(company_Symbol,company_Symbol);
    loadFavouriteList();
}

//To generate high charts
function generateHistoricalChart() {
    var Markit = {};
        Markit.InteractiveChartApi = function(symbol,duration){
        this.symbol = symbol.toUpperCase();
        this.duration = duration;
        this.PlotChart();
    };

    Markit.InteractiveChartApi.prototype.PlotChart = function(){

        var params = {
            parameters: JSON.stringify( this.getInputParams() )
        }

        //Make JSON request for timeseries data
        $.ajax({
            beforeSend:function(){
                $("#chartDemoContainer").text("Loading chart...");
            },
            data: params,
            url: "http://dev.markitondemand.com/Api/v2/InteractiveChart/jsonp",
            dataType: "jsonp",
            context: this,
            success: function(json){
                //Catch errors
                if (!json || json.Message){
                    console.error("Error: ", json.Message);
                    return;
                }
                this.render(json);
            },
            error: function(response,txtStatus){
                console.log(response,txtStatus)
            }
        });
    };

    Markit.InteractiveChartApi.prototype.getInputParams = function(){
        return {  
            Normalized: false,
            NumberOfDays: this.duration,
            DataPeriod: "Day",
            Elements: [
                {
                    Symbol: this.symbol,
                    Type: "price",
                    Params: ["ohlc"] //ohlc, c = close only
                },
                {
                    Symbol: this.symbol,
                    Type: "volume"
                }
            ]
            //,LabelPeriod: 'Week',
            //LabelInterval: 1
        }
    };

    Markit.InteractiveChartApi.prototype._fixDate = function(dateIn) {
        var dat = new Date(dateIn);
        return Date.UTC(dat.getFullYear(), dat.getMonth(), dat.getDate());
    };

    Markit.InteractiveChartApi.prototype._getOHLC = function(json) {
        var dates = json.Dates || [];
        var elements = json.Elements || [];
        var chartSeries = [];

        if (elements[0]){

            for (var i = 0, datLen = dates.length; i < datLen; i++) {
                var dat = this._fixDate( dates[i] );
                var pointData = [
                    dat,
                    elements[0].DataSeries['open'].values[i],
                    elements[0].DataSeries['high'].values[i],
                    elements[0].DataSeries['low'].values[i],
                    elements[0].DataSeries['close'].values[i]
                ];
                chartSeries.push( pointData );
            };
        }
        return chartSeries;
    };

    Markit.InteractiveChartApi.prototype._getVolume = function(json) {
        var dates = json.Dates || [];
        var elements = json.Elements || [];
        var chartSeries = [];

        if (elements[1]){

            for (var i = 0, datLen = dates.length; i < datLen; i++) {
                var dat = this._fixDate( dates[i] );
                var pointData = [
                    dat,
                    elements[1].DataSeries['volume'].values[i]
                ];
                chartSeries.push( pointData );
            };
        }
        return chartSeries;
    };

            $.ajax({
              url: "http://stockdetails-1274.appspot.com/",
              cache: false,
              data: {
                ChartSymbol: company_Symbol
              },
              dataType: "json",
              type: 'GET',
              success: function( data ) {
                // Create the chart
                    var ohlc = Markit.InteractiveChartApi.prototype._getOHLC(data),
                    volume = Markit.InteractiveChartApi.prototype._getVolume(data);

                // create the chart
                $('#container').highcharts('StockChart', {

                    rangeSelector: {
                        buttons: [{
                                    type: 'week',
                                    count: 1,
                                    text: '1w'
                                }, {
                                    type: 'month',
                                    count: 1,
                                    text: '1m'

                                }, {
                                    type: 'month',
                                    count: 3,
                                    text: '3m'
                                }, {
                                    type: 'month',
                                    count: 6,
                                    text: '6m'

                                }, {
                                    type: 'year',
                                    count: 1,
                                    text: '1y'
                                }, {
                                   type: 'ytd',
                                   text: 'YTD'
                                }, {
                                    type: 'all',
                                    text: 'All'
                                }],
                        selected: 0,
                        inputEnabled: false
                    },

                    title : {
                        text : company_Symbol+' Stock Value'
                    },

                    yAxis: [{
                        title: {
                            text: 'Stock Value'
                        },
                        height: 200,
                        lineWidth: 2
                    }, {
                        top: 300,
                        height: 100,
                        offset: 0,
                        lineWidth: 2
                    }],

                    series: [{
                        type: 'area',
                        name : company_Symbol,
                        data: ohlc,
                        tooltip: {
                            valueDecimals: 2
                        },
                        fillColor : {
                            linearGradient : {
                                x1: 0,
                                y1: 0,
                                x2: 0,
                                y2: 1
                            },
                        stops : [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                            ]
                        }
                    }, {
                        type: 'column',
                        name: 'Volume',
                        data: volume,
                        yAxis: 1,
                    }],
                });

              }
            }); 
}

var company_Symbol="";
var html_text="";
var last_price="", company_name="", change="", change_percent="";
var stock_price="";
var changeValue="";

//jQuery function for autocomplete and ajax request to PHP file residing on AWS
  $(document).ready(function(){
      loadFavouriteList();

      var $SymbolErr = false;
      var $flag=true;
    
    $("#gotoStockDetails").addClass("disabled");
      
    // Manual refresh 
    $("#refreshManually").on("click",function(){
       updateValues(); 
    });
    
    //Auto refresh stock details every 5 seconds
    $(function() {
        var interval=null;
        $('#toggle-event').change(function() {
            if($(this).prop('checked')==true) {
                interval=setInterval(updateValues,5000);
            } else {
                clearInterval(interval); // stop the interval
            }
        })
    });
      
    $( "#symbol" ).autocomplete({
      source: function( request, response ) {
        $.ajax({
          url: "http://stockdetails-1274.appspot.com/",
          cache: false,
          data: {
            CompanySymbol: request.term
          },
          dataType: "json",
          type: 'GET',
          success: function( data ) {
              var items=[];
              for (var x = 0; x < data.length; x++) {
                content = data[x].Symbol;
                content += " - ";
                content += data[x].Name;
                content += " ( "+data[x].Exchange+" )";
                items.push(content);
            }
            response(items);
          }
        });
      },
      minLength: 1,
      select: function( event, ui ) {
        event.preventDefault();
        console.log( ui.item ?
          "Selected: " + ui.item.label :
          "Nothing selected, input was " + this.value);
        if(ui.item) {
            company_Symbol=ui.item.label.split(" ")[0];
            $SymbolErr=true;
            $flag=true;
            $("#symbol").val(company_Symbol);  
            console.log(company_Symbol);
        }
      }
    });
      
    $("#gotoStockDetails").on("click",function() {
        if(company_Symbol) {
            $('#myCarousel').carousel(1);       
        } else {
            $("#gotoStockDetails").addClass("disabled");
        }
    });
      
    $("#resetButton").on("click",function() {
        $('#myCarousel').carousel(0);   
    });
           
           
    $("#gotoListButton").on("click", function () {
        $('#myCarousel').carousel(0);
        $("#gotoStockDetails").removeClass("disabled");
    });
      
    $("#submitButton").on("click", function () {
        
        if($(".addFavouriteButton").hasClass('glyphicon-star')) {
            $(".addFavouriteButton").removeClass('glyphicon-star').addClass('glyphicon-star-empty');   
        }
        
        if($("#symbol").val().trim()!="" && !$SymbolErr) {
            $(".symbolError").html("Select a valid entry");
        } else if($("#symbol").val().trim()=="") {
            $(".symbolError").html("Please fill out this field");
        } else {
            
            for(var i=0;i<localStorage.length;i++){
                var s=localStorage.getItem(localStorage.key(i));
                if(s==company_Symbol) {
                   // alert(s);
                    if($(".addFavouriteButton").hasClass('glyphicon-star-empty')){
                        $(".addFavouriteButton").removeClass('glyphicon-star-empty').addClass('glyphicon-star');
                        $(".addFavouriteButton").css("color", "#FFF000");
                    }
                }
            }
            
            $('#myCarousel').carousel(1);
            if($flag) {
                $.ajax({
                  url: "http://stockdetails-1274.appspot.com/",
                  cache: false,
                  data: {
                    CompanySymbol: company_Symbol,
                    getQuote: true
                  },
                  dataType: "json",
                  type: 'GET',
                  success: function( data ) {
                      $flag=false;
                      html_text="";
                      last_price=Math.round(data.LastPrice * 100)/100;
                      company_name=data.Name;
                      current_stock_img="http://chart.finance.yahoo.com/t?s="+data.Symbol;
                      change=Math.round(data.Change * 100)/100;
                      change_percent=Math.round(data.ChangePercent * 100)/100;
                      html_text+="<div class='col-md-10' id='Searchresult'><div class='container'><ul class='nav nav-pills'><li class='active'><a href='#CurrentStock' data-toggle='tab'><span class='glyphicon glyphicon-dashboard' aria-hidden='true'></span> Current Stock</a></li><li><a href='#HistoricalCharts' data-toggle='tab'><span class='glyphicon glyphicon-stats' aria-hidden='true'></span> Historical Charts</a></li><li><a href='#NewsFeeds' data-toggle='tab'><span class='glyphicon glyphicon-link' aria-hidden='true'></span> News Feeds</a></li></ul><hr /><div class='tab-content'><div class='tab-pane active' id='CurrentStock'>";
                    html_text+="<div class='container row'><div class='col-md-5'><p><b>Stock Details</b></p><div class='table-responsive'><table class='table table-striped' id='myTable1'><tbody><tr><td><b>Name</b></td><td>"+data.Name+"</td></tr>";
                      html_text+="<tr><td><b>Symbol</b></td><td>"+data.Symbol+"</td></tr>";
                      html_text+="<tr><td><b>Last Price</b></td><td>$ "+Math.round(data.LastPrice * 100)/100+"</td></tr>";
                      html_text+="<tr><td><b>Change (Change Percent)</b></td>";
                      if((Math.round(data.ChangePercent * 100)/100) > 0) {
                        html_text+="<td style='color: green'>"+Math.round(data.Change * 100)/100+" ( "+Math.round(data.ChangePercent * 100)/100+"% )<img src='http://cs-server.usc.edu:45678/hw/hw8/images/up.png' width='20' height='20'></td></tr>";   
                      } else {
                        html_text+="<td style='color: red'>"+Math.round(data.Change * 100)/100+" ( "+Math.round(data.ChangePercent * 100)/100+"% )<img src='http://cs-server.usc.edu:45678/hw/hw8/images/down.png' width='20' height='20'></td></tr>";
                      }
                      var myDate=new Date(data.Timestamp);
                      var monthNames = ["January", "February", "March", "April", "May", "June","July", "August", "September",    "October","November", "December"];
                      html_text+="<tr><td><b>Time and Date</b></td><td>"+ myDate.getDate() + " " + monthNames[myDate.getMonth()] +" "+ myDate.getFullYear()+","+zeroFill(myDate.getHours()) + ":" + zeroFill(myDate.getMinutes()) + ":" + zeroFill(myDate.getSeconds())+(myDate.getHours>=12?' AM':' PM')+"</td></tr>";
                      html_text+="<tr><td><b>Market Cap</b></td>";
                      if((data.MarketCap/1000000000) < 0.01) {
                        html_text+="<td>"+Math.round(((data.MarketCap/1000000000)*1000)*100)/100+" Million</td></tr>";
                      } else {
                        html_text+="<td>"+Math.round((data.MarketCap/1000000000)*100)/100+" Billion</td></tr>";
                      }
                      html_text+="<tr><td><b>Volume</b></td><td>"+data.Volume+"</td></tr>";
                      html_text+="<tr><td><b>Change YTD (Change Percent YTD)</b></td>";
                      if((Math.round(data.ChangePercentYTD * 100)/100) > 0) {
                        html_text+="<td style='color: green'>"+Math.round(data.ChangeYTD * 100)/100+" ( "+Math.round(data.ChangePercentYTD * 100)/100+"% )<img src='http://cs-server.usc.edu:45678/hw/hw8/images/up.png' width='20' height='20'></td></tr>";      
                      } else {
                        html_text+="<td style='color: red'>"+Math.round(data.ChangeYTD * 100)/100+" ( "+Math.round(data.ChangePercentYTD * 100)/100+"% )<img src='http://cs-server.usc.edu:45678/hw/hw8/images/down.png' width='20' height='20'></td></tr>";
                      }
                      html_text+="<tr><td><b>High Price</b></td><td>$ "+Math.round(data.High * 100)/100+"</td></tr>";
                      html_text+="<tr><td><b>Low Price</b></td><td>$ "+Math.round(data.Low * 100)/100+"</td></tr>";
                      html_text+="<tr><td><b>Opening Price</b></td><td>$ "+Math.round(data.Open * 100)/100+"</td></tr>";
                      html_text+="</tbody></table></div></div><div class='col-md-7'><div class='row'><div class='col-md-4 col-md-offset-7'><a href='javascript:fbPost();'><img id=shareButton class='img-responsive' style='display: inline-block' src='fb_icon.png' width='35em' height='35em'></a>&nbsp;&nbsp;<a href='javascript:addFavouriteItem();'><button type='button' class='btn btn-default'><span class='glyphicon glyphicon-star-empty addFavouriteButton'></span></button></a></div></div><img class='img-responsive' src='http://chart.finance.yahoo.com/t?s="+data.Symbol+"&lang=en-US&width=480&height=380'>";
                      html_text+="</div></div></div>";
                      html_text+="<div class='tab-pane' id='HistoricalCharts'><div class='container row'><div class='col-md-12'><div id='container' style='height: 60%; width: 65%;'></div></div></div></div>";
                      generateHistoricalChart();
                      html_text+="<div class='tab-pane' id='NewsFeeds'><div class='container row'><div class='col-md-12'><div id='newsfeed' style='height: 50%;'></div></div></div>";
                      
                      $.get("http://stockdetails-1274.appspot.com/?CompanyNews="+company_Symbol, function(data){
                         if(data) {
                             console.log("News Feed");
                             var data_list = JSON.parse(data);
                             for (var x = 0; x < data_list.d.results.length; x++) {
                                var str = data_list.d.results[x].Description; 
                                var re = new RegExp(company_Symbol,"g");
                                var res = str.replace(re, "<b>"+company_Symbol+"</b>");
                                 var newsDate=new Date(data_list.d.results[x].Date);
                                 $('#newsfeed').append("<div style='background-color: #F0F0F0;border-radius: 0.25em;'><div style='padding: 1em 2em 1em 1em;'><a href='"+data_list.d.results[x].Url+"'>"+data_list.d.results[x].Title+"</a><br /><br /><div style='width: 80%'>"+res+"</div><br />Publisher: "+data_list.d.results[x].Source+"<br /><br />Date: "+newsDate.getDate() + " " + monthNames[newsDate.getMonth()] +" "+ newsDate.getFullYear()+" "+zeroFill(newsDate.getHours())+ ":" + zeroFill(newsDate.getMinutes()) + ":" + zeroFill(newsDate.getSeconds())+"</div></div><br />");
                             }
                             
                         } 
                      });   
                       html_text+="</div></div></div></div>";
                      $('#stockDetails').html(html_text);
                  }
                });
            }
        }
    });
      
    
});

