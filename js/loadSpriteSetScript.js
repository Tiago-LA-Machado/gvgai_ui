var ul = document.getElementById("spriteList");
        
        //access the serve in order to get the sprite set of a game
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function() {

            if (this.readyState == 4 && this.status == 200) {

                var myObj = JSON.parse(this.responseText);
                for(var i = 0; i < myObj.length; i++)
                {
                    getObjectData(myObj[i], ul);
                }
            }
        };
        xmlhttp.open("GET", "http://localhost:9000/spriteSet", true);
        xmlhttp.send();

        //Extracts all  information of a sprite and add it to a list element
        function getObjectData(obj, upperUl)
        {
            var currentObj = obj;
            var identifier = currentObj.identifier;
            var parameters = currentObj.parameters;
            var imgSrc = document.createElement("img");
            for (var i = 0; i < parameters.length; i++) {
                if("img" in parameters[i])
                {
                    var imgPathForUrlCreation = parameters[i]["img"] + ".png";
                    fetchBlob(imgPathForUrlCreation, imgSrc);
                }
            }

            var li = document.createElement("li");
            li.appendChild(document.createTextNode(identifier));
            li.appendChild(imgSrc);
            upperUl.appendChild(li);

            var objChildren = currentObj.children;
            if(objChildren)
            {
                for(var j = 0; j < objChildren.length; j++)
                {
                    var innerCurrentObj = objChildren[j];
                    var innerUl = document.createElement("ul");
                    getObjectData(innerCurrentObj, innerUl);
                    upperUl.appendChild(innerUl);
                }
            }
        }

        //get the image of an specific sprite
        function fetchBlob(imgPath, imgElement) {
            // construct the URL path to the image file from the product.image property
            var url = imgPath;
            // Use XHR to fetch the image, as a blob
            // Again, if any errors occur we report them in the console.
            var request = new XMLHttpRequest();
            var params = "picture=" + imgPath;
            request.open('GET', "http://localhost:9000/imgs" + "?" + params, true);
            request.responseType = 'blob';

            request.onload = function() {
                if(request.status === 200) {
                // Convert the blob to an object URL — this is basically an temporary internal  URL
                // that points to an object stored inside the browser
                var blob = request.response;
                objectURL = URL.createObjectURL(blob);
                // invoke showProduct
                imgElement.src = objectURL;
            } else {
                alert('Network request for "' + product.name + '" image failed with response ' +     request.status + ': ' + request. statusText   );
            }
        };  

        request.send();
        console.log("finished");