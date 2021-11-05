toggleMenu = 0

function menubar() {
	if(toggleMenu == 0) {
		document.getElementById('navigation').style.marginLeft = "0px";
		toggleMenu = 1;
	} else {
		document.getElementById('navigation').style.marginLeft = "-188px";
		toggleMenu = 0;
	}
}

$(function() {
  $('a[href*=#]:not([href=#])').click(function() {
    if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
      var target = $(this.hash);
      target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
      if (target.length) {
        $('html,body').animate({
          scrollTop: target.offset().top
        }, 1000);
        return false;
      }
    }
  });
});

g = 0;
p = 0;
a1 = 0;
a2 = 0;
pb1 = 0;
pb2 = 0;
sa = 0;
sb = 0;

function calculateBase() {
	roots(getPrime());
}

function calculatePublicKey() {
	g = document.getElementById('g').value;
	p = document.getElementById('p').value;
	a1 = document.getElementById('a1').value;
	a2 = document.getElementById('a2').value;
	getPublicKeyA();
	getPublicKeyB();
}

function calculateSharedKey() {
	getSharedKeyA();
	getSharedKeyB();
}

function getEncryptedMessages() {
	document.getElementById('e-result').innerHTML = ""+cipher(0, document.getElementById('plain-messages').value, sa);
}

function getSharedKeyA() {
	var x = bigInt(pb2).pow(a1).mod(p);
	document.getElementById('s-a').innerHTML = x;
	sa = x;
}

function getSharedKeyB() {
	var x = bigInt(pb2).pow(a1).mod(p);
	document.getElementById('s-b').innerHTML = x;
	sb = x;
}

function getPublicKeyA() {
	var x = bigInt(g).pow(a1).mod(p);
	document.getElementById('pb-a').innerHTML = x;
	pb1 = x;
}

function getPublicKeyB() {	
	var x = bigInt(g).pow(a2).mod(p);
	document.getElementById('pb-b').innerHTML = x;
	pb2 = x;
}

function roots(n) {	
	var o = 1;
	var k;
	var roots = new Array();
	var z = 0;
	
	for (var r = 2; r < n; r++) {
		k = Math.pow(r, o);
		k %= n;
		while (k > 1) {
			o++;
			k *= r;
			k %= n;
		}
		if (o == (n - 1)) {
			roots[z] = r;
			z++;
		}
		o = 1;
	}

	var smallest = roots[0];
	for(var r = 0; r < roots.length; r++) {
		if(roots[r] < smallest) smallest = roots[r];
	}
	
	document.getElementById('g').value = smallest;
	g = smallest;
}

function getPrime() {
	var y = getRandomInt(11, 54);

	while(!isPrime(y)) {
		y = getRandomInt(11, 54);
	}

	document.getElementById('p').value = y;
	p = y;
	return y;
}

/**
 * Returns a random integer between min (inclusive) and max (inclusive)
 * Using Math.round() will give you a non-uniform distribution!
 */
function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function isPrime(n) {
	var i = 2;
	while (i <=  (n / 2) ) {
		if(n % i == 0) return false;
		i++;
	}
	return true;
}

function cipher(mode_cipher, msg, n_shift) {
	msg = msg.toUpperCase();
	var result = new Array();

	if(mode_cipher == 0) /* encrypt mode */ {
		for(var ii = 0; ii < msg.length; ii++){
			var x = msg.charAt(ii).charCodeAt(); // 'A' = 65, 'Z' = 90
			var shifted_result = (((x - 65) + n_shift) % 26) + 65;
			result[ii] = String.fromCharCode(shifted_result);
		}
	} else /* decrypt mode */ {

	}

	var str_result = "";
	for (var i = 0; i < result.length; i++) {
	    str_result += result[i]+"";
	}

	return str_result;
}
