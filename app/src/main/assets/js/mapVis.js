const DANGER_COLOUR = 'rgb(187, 15, 23)';
const VERY_HOT_COLOUR = 'rgb(255, 127, 39)';
const HOT_COLOUR = 'rgb(255, 242, 0)';
const NORMAL_COLOUR = 'rgb(255, 255, 255)';
const UNDEFINED_COLOUR = 'rgb(195, 195, 195)';

isRealView = false;
tempData = 0;
nhsData = 0;

function loadVisualisation(temp_data, nhs_data, map_data, width, height) {
    var svg = d3.select('#vis');
    var outer_padding = 10;

    tempData = temp_data;
    nhsData = nhs_data;

    document.getElementById("vis").onclick = switchView;

    showVisualisation(temp_data, nhs_data, map_data, svg, width, height);
}

function countyTempToColour(id, dataset) {

    if (dataset[id] == null) {
        console.log(id);
        return UNDEFINED_COLOUR;
    }
    var countyTemps = dataset[id].temps;
    var averageTemp = countyTemps.reduce( (p, c) => p + c, 0 ) / countyTemps.length;
    var colour = NORMAL_COLOUR;

    if (averageTemp > 37.2 && averageTemp <= 37.6) {
        colour = HOT_COLOUR;
    } else if (averageTemp > 37.6 && averageTemp <= 37.9) {
        colour = VERY_HOT_COLOUR;
    } else if (averageTemp > 37.9) {
        colour = DANGER_COLOUR;
    }

    return colour;
}

function countyCaseToColour(id, dataset) {
    if (dataset[id] == null) {
        console.log(id);
        return HOT_COLOUR;
    }

    var countyCaseRates = dataset[id].cases;
    var rateDifference = (countyCaseRates[0] - countyCaseRates[dataset[id].count - 1]) / countyCaseRates[0];
    var colour = 255 * (1 - rateDifference);

    return "rgb(" + colour + ", " + colour + ", " + colour + ")";

}

function switchView() {
    var map = d3.select('#map');
    if (isRealView) {
        map.selectAll("path").each(function(d) {
            d3.select(this).attr('fill', function(d) {return countyTempToColour(d.id, tempData)})
        });
    } else {
        map.selectAll("path").each(function(d) {
            d3.select(this).attr('fill', function(d) {return countyCaseToColour(d.id, nhsData)})
        });
    }
    isRealView = !isRealView;
}

function showVisualisation( temp_data, nhs_data, map_data, svg, width, height, countyColours ) {
    svg.attr('width', width).attr('height', height);

    var map = svg.append('g').attr('id', 'map');
    var counties = svg.append('g').attr('id', 'counties');
    var shapes = svg.append('g').attr('id', 'shapes');
    var projection = d3.geoMercator()
        .center([-4.1826, 56.8169])
        .rotate([0, 0])
        .scale(3000)
        .translate([width / 2, height / 2]);
    var path =  d3.geoPath().projection(projection);

    map.append("g").selectAll("path").data(topojson.feature(map_data, map_data.objects.lad).features).enter().append("path")
        .attr("d", path).attr('fill', function(d) {return countyTempToColour(d.id, temp_data)})
        .attr('stroke', 'black').attr('id', function(d) {return d.id;});
}