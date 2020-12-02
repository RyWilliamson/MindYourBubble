SVG_PATH = "file:///android_asset/svg/";

const DANGER_COLOUR = 'rgb(187, 15, 23)';
const VERY_HOT_COLOUR = 'rgb(255, 127, 39)';
const HOT_COLOUR = 'rgb(255, 242, 0)';
const NORMAL_COLOUR = 'rgb(0, 0, 0)';
const UNDEFINED_COLOUR = 'rgb(195, 195, 195)';

function loadVisualisation(dataset, width, height) {
    var svg = d3.select('#vis');
    var outer_padding = 10;
    var plot_oval = {
        rx: (width - outer_padding * 2) / 2,
        ry: (height - outer_padding * 2) / 2,
        cx: width / 2,
        cy: height / 2
    };
    bubble_centres = computeBubbleCoords(dataset.length, plot_oval);
    bubble_map = bubbleMap(dataset);
    showVisualisation(dataset, svg, width, height, plot_oval, bubble_centres);
}

function tempToColour(temperature) {

    if (temperature == null) {
        return UNDEFINED_COLOUR;
    }

    var colour = NORMAL_COLOUR;

    if (temperature > 37.2 && temperature <= 37.6) {
        colour = HOT_COLOUR;
    } else if (temperature > 37.6 && temperature <= 37.9) {
        colour = VERY_HOT_COLOUR;
    } else if (temperature > 37.9) {
        colour = DANGER_COLOUR;
    }

    return colour;
}

function tempToImage(temperature) {
    var colour = tempToColour(temperature)

    switch(colour) {
        case NORMAL_COLOUR:
            return SVG_PATH + 'user_normal.svg'
        case HOT_COLOUR:
            return SVG_PATH + 'user_hot.svg'
        case VERY_HOT_COLOUR:
            return SVG_PATH + 'user_veryhot.svg'
        case DANGER_COLOUR:
            return SVG_PATH + 'user_danger.svg'
        default:
            return SVG_PATH + 'user_undefined.svg'
    }
}

function bubbleMap(dataset) {
    dict = {}
    for (var i = 0; i < dataset.length; i++) {
        dict[dataset[i].name] = i;
    }
    return dict;
}

// Adapted from https://stackoverflow.com/questions/58534293/how-can-i-distribute-points-evenly-along-an-oval
function computeBubbleCoords(bubble_count, plot_oval) {
    centres = [];
    var inward_x = 52;
    var inward_y = -6;
    var a = plot_oval.rx;
    var b = plot_oval.ry;
    for (i = 3; i < bubble_count + 3; i++) {
        var t = (2 * Math.PI * i) / bubble_count;
        t = t + 0.3 * Math.atan(( (a - b) * Math.tan(t) ) / ( a + b * Math.pow( Math.tan(t), 2) ) );
        var sq = 1.0 / Math.hypot(a * Math.sin(t), b * Math.cos(t));
        var x = Math.round(plot_oval.cx + (a - b * inward_x * sq) * Math.cos(t));
        var y = Math.round(plot_oval.cy + (a - b * inward_y * sq) * Math.sin(t));
        centres.push([x, y]);
    }
    return centres;
}

function showVisualisation( dataset, svg, width, height, plot_oval, bubble_centres) {
    svg.attr('width', width).attr('height', height);
    var radius = 50;
    //drawEllipse(svg, plot_oval.cx, plot_oval.cy, plot_oval.rx, plot_oval.ry, 'black', 0, 'black');
    for (var i = 0; i < dataset.length; i++) {
        drawConnections(svg, dataset[i], dataset[i].known_links, bubble_centres, radius, bubble_map);
    }
    for (var i = 0; i < dataset.length; i++) {
        drawBubble(svg, bubble_centres[i], dataset[i], radius);
    }
}

function drawConnections(svg, data, links, centres, radius, bubble_map) {
    var g = svg.append('g');
    for (var i = 0; i < links.length; i++) {
        start = centres[bubble_map[data.name]];
        end = centres[bubble_map[links[i]]];
        lineBetweenPoints(g, start[0], start[1], end[0], end[1]);
    }
}

function lineBetweenPoints(svg, x1, y1, x2, y2) {
    svg.append('line').attr('x1', x1).attr('y1', y1)
        .attr('x2', x2).attr('y2', y2)
        .attr('stroke', 'black').attr('stroke-width', 2);
}

function drawLine(svg, x, y, length, angle) {
    var coords = getCoordCircumference(x, y, length, angle)
    var nx = coords[0];
    var ny = coords[1];
    lineBetweenPoints(svg, x, y, nx, ny);
    return [nx, ny];
}

function drawEllipse(svg, cx, cy, rx, ry, colour, opacity, line_colour) {
    svg.append('ellipse')
        .attr('cx', cx)
        .attr('cy', cy)
        .attr('rx', rx)
        .attr('ry', ry)
        .attr('fill', colour)
        .attr('fill-opacity', opacity)
        .attr('stroke', line_colour)
        .attr('stroke-width', 4);
}

function bubbleHeader(svg, text, size, cx, cy, rx, ry, text_colour) {
    svg.append('text')
        .attr('colour', text_colour)
        .attr('text-anchor', 'middle')
        .attr('x', cx)
        .attr('y', cy-ry/2)
        .text(text);
}

function otherText(svg, text, size, cx, cy, rx, ry, text_colour) {
    svg.append('text')
        .attr('colour', text_colour)
        .attr('text-anchor', 'middle')
        .attr('x', cx)
        .attr('y', cy+ry/2)
        .text(text);
}

function drawPeopleBubbles(svg, data, cx, cy, r, line_colour) {
    var people = data.visible_people;
    var g = svg.append('g');
    var defs = g.append('defs');
    var split = Math.floor(r / 2) - 1;
    var half_split = Math.floor(split / 2)
    for (var i = 0; i < people.length; i++) {
        if (i >= 8) {break;}
        var row = Math.floor(i / 3) + 1;
        var col = (i % 3) + 1;
        var x;
        var y = ((row - 2) * split) + half_split + cy - 2;

        if (row == 3) {
            x = ((col - 2) * split) + half_split + cx;
        } else {
            x = ((col - 2) * split) + cx;
        }

        drawPerson(g, defs, data, i, x, y, 8, "black", 0, "black");
    }
}

function drawPerson(svg, defs, data, personNo, cx, cy, r, colour, opacity, line_colour) {
    var id = personNo + data.name + data.visible_people[personNo].name;
    var clip_path = defs.append('clip_path').attr('id', id);
    clip_path.append('circle').attr('cx', cx).attr('cy', cy).attr('r', r);
    svg.append('image').attr('x', cx - r).attr('y', cy - r).attr('width', r*2).attr('height', r*2)
        .attr('href', tempToImage(data.avg_temp)).attr('clip-path', 'url(#' + id + ')');
}

function getCoordCircumference(cx, cy, r, angle) {
    var rads = angle * Math.PI / 180;
    var x = cx + r * Math.cos(rads);
    var y = cy + r * Math.sin(rads);
    return [x, y];
}

function drawOtherConnections(svg, connection_no, cx, cy, r) {
    if (connection_no != 0) {
        var g = svg.append('g');
        var angle = 180;
        var new_r = 10;
        circums = getCoordCircumference(cx, cy, r, angle);
        new_circums = drawLine(g, circums[0], circums[1], 20, angle);
        c_c = getCoordCircumference(new_circums[0], new_circums[1], new_r, angle);
        drawEllipse(g, c_c[0], c_c[1], new_r, new_r, 'white', 1, 'black');
        otherText(g, connection_no, 8, c_c[0], c_c[1], new_r, new_r, 'black');
    }
}

function drawBubble(svg, main_centre, data, r) {
    var x = main_centre[0];
    var y = main_centre[1];
    var g = svg.append('g').attr('id', data.name);
    drawEllipse(g, x, y, r, r, 'white', 1, tempToColour(data.avg_temp));
    bubbleHeader(g, data.name, 8, x, y, r, r, 'black');
    drawPeopleBubbles(g, data, x, y, r, 'black');
    drawOtherConnections(g, data.other_links, x, y, r);
}
