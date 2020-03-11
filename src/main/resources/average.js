//计算成交量代码, from: https://beastlybeast.github.io/SignificantTrades/index.html
function average(arr, priceFilter) {
    var sumSizeArr = {}, sumPriceArr = {}, results = [], timestamp, side;
    for (var i = 0; i < arr.length; i++) {
        var item = arr[i];
        var key = item.timestamp + item.side;
        if (!(key in sumSizeArr)) {
            sumSizeArr[key] = {};
            sumSizeArr[key].sizeSum = item.size;
        } else {
            sumSizeArr[key].sizeSum += arr[i].size;
        }
    }

    for (var i = 0; i < arr.length; i++) {
        var item = arr[i];
        var key = item.timestamp + item.side;
        var sizeItem = sumSizeArr[key];
        if (!(key in sumPriceArr)) {
            sumPriceArr[key] = {};
            sumPriceArr[key].symbol = item.symbol;
            sumPriceArr[key].priceSum = item.price * item.size / sizeItem.sizeSum;
            sumPriceArr[key].sizeSum = item.size;
            sumPriceArr[key].timestamp = formatAMPM(new Date(item.timestamp));
            sumPriceArr[key].side = item.side;
        } else {
            sumPriceArr[key].priceSum += item.price * item.size / sizeItem.sizeSum;
            sumPriceArr[key].sizeSum += item.size;
        }
    }

    for (key in sumPriceArr) {
        var item = sumPriceArr[key];
        if (item.sizeSum >= priceFilter) {
            results.push({
                symbol: item.symbol,
                price: item.priceSum.toFixed(2),
                size: item.sizeSum,
                timestamp: item.timestamp,
                side: item.side
            });
        }
    }
    return results;
}