/**
 * Created by tiagomachado on 5/21/18.
 */
function createDivSpanElements(divId, spanId, innerHtmlText)
{
    var divElement = document.createElement('div');
    divElement.classList.add("distance");
    divElement.id = divId;
    var spanElement = document.createElement('span');
    spanElement.id = spanId;
    spanElement.innerHTML = innerHtmlText;
    spanElement.classList.add("spanCenteredText");
    spanElement.classList.add("checkBoxSpan");
    divElement.appendChild(spanElement);
    specialParameters.appendChild(divElement);
}

function createAmmoSelectList(divToBePartOf, listId)
{
    var divStypeAmmoSelect = document.createElement("div");
    divStypeAmmoSelect.classList.add("styled-select");
    divStypeAmmoSelect.classList.add("blue");
    divStypeAmmoSelect.classList.add("rounded");

    var ammoSpritesCollection = retrievingAmmoSprites(stypeCollection);
    console.log(ammoSpritesCollection);

    var listAmmoResource = document.createElement("select");
    listAmmoResource.id = listId;
    divStypeAmmoSelect.appendChild(listAmmoResource);

    for (var i = 0; i < ammoSpritesCollection.length; i++) {
        var option = document.createElement("option");
        spriteName = ammoSpritesCollection[i];
        option.value = spriteName;
        option.text = spriteName;
        listAmmoResource.appendChild(option);
    }

    div = document.getElementById(divToBePartOf);
    div.appendChild(divStypeAmmoSelect);

}

function createStypeSelectList(divToBePartOf, listId)
{
    var divStypeSelect = document.createElement("div");
    divStypeSelect.classList.add("styled-select");
    divStypeSelect.classList.add("blue");
    divStypeSelect.classList.add("rounded");

    var stypeCollection = retrieveStypeOptions();

    var listStype = document.createElement("select");
    listStype.id = listId;
    divStypeSelect.appendChild(listStype);


    for (var i = 0; i < stypeCollection.length; i++) {
        var option = document.createElement("option");
        stypeObject = stypeCollection[i];
        option.value = stypeObject.name;
        option.text = stypeObject.name;
        listStype.appendChild(option);
    }

    divOrientation = document.getElementById(divToBePartOf);
    divOrientation.appendChild(divStypeSelect);

}

function createOrientationSelectList(divToBePartOf, selectId)
{
    var divOrientationSelect = document.createElement("div");
    divOrientationSelect.classList.add("styled-select");
    divOrientationSelect.classList.add("blue");
    divOrientationSelect.classList.add("rounded");

    var orientationCollection = ['NONE', 'LEFT', 'RIGHT', 'UP', 'DOWN'];

    var listOrientation = document.createElement("select");
    listOrientation.id = selectId;
    divOrientationSelect.appendChild(listOrientation);


    for (var i = 0; i < orientationCollection.length; i++) {
        var option = document.createElement("option");
        var orientationObject = orientationCollection[i];
        option.value = orientationObject;
        option.text = orientationObject;
        listOrientation.appendChild(option);
    }

    divOrientation = document.getElementById(divToBePartOf);
    divOrientation.appendChild(divOrientationSelect);

}

function createInputNumber(divToAddId, type, min, max, valueToShow, inputId)
{
    var inputNumber = document.createElement("input");
    inputNumber.id = inputId;
    inputNumber.classList.add('input-spacing');
    inputNumber.type = type;
    inputNumber.min = min;
    inputNumber.max = max;
    inputNumber.value = valueToShow;

    var div = document.getElementById(divToAddId);
    div.appendChild(inputNumber);
}

function createInputRange(divToAddId, type, min, max, step, spanId, valueToShow, inputId)
{
    var inputRange = document.createElement("input");
    inputRange.id = inputId;
    inputRange.classList.add('bars');
    inputRange.classList.add('sliders');
    inputRange.type = type;
    inputRange.min = min;
    inputRange.max = max;
    inputRange.step = step;
    inputRange.value = valueToShow;

    var spanElement = document.createElement('span');
    spanElement.id = spanId;
    spanElement.style.cssFloat = 'right';
    spanElement.innerHTML = valueToShow;
    spanElement.classList.add("spanCenteredText");
    spanElement.classList.add("checkBoxSpan");

    inputRange.addEventListener("input", function()
    {
        spanElement.innerHTML = inputRange.value;
    });

    var div = document.getElementById(divToAddId);
    div.appendChild(spanElement);
    div.appendChild(inputRange);
}

function updateInputNumber(parameterValue, inputId)
{
    var input = document.getElementById(inputId);
    input.value = parameterValue;
}

function updateInputRange(parameterValue, inputId, spanRangeId)
{
    var inputRange = document.getElementById(inputId);
    var spanRange = document.getElementById(spanRangeId);
    inputRange.value = parameterValue;
    spanRange.innerHTML = parameterValue;
}

function removeElements(parentElement)
{
    while (parentElement.firstChild) {
        parentElement.removeChild(parentElement.firstChild);
    }
}

function updateSelectParameter(selectId, parameterValue)
{
    var valueToSelect = parameterValue;
    var select = document.getElementById(selectId);
        var opts = select.options;
        for (var opt, j = 0; opt = opts[j]; j++) {
            if (opt.value == valueToSelect) {
                select.selectedIndex = j;
                break;
            }
        }
}