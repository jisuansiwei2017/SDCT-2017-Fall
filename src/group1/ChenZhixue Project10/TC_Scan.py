def Change(L,t):
	a = (int)(log(len(L))/log(2))+1
	while len(L)<pow(2,a):
		L.append(t)

def max(x,y): # Here we should use a binary associative function f
	if x<y:
		return y
	return x
def plus(x,y):
	return x+y
def mul(x,y):
	return x*y


def getintlog(a,t):
	n=0
	while(a>=t):
		n+=1
		a=a/t
	return n

def Reduce(L,f): # Where we already assume that len(L) is 2^n
	n,a = 0,len(L)
	n=getintlog(a,2)
	if(n==1):
		return n
	for i in range(1,n+1):
		for j in range(0,len(L)):
			if((j+1)%(pow(2,i))==0):
				L[j]=f(L[j],L[j-pow(2,i-1)])
	return n

def Reduce1(L,f):
	a=len(L)
	n=getintlog(a,2)
	if(a==1):
		return 
	for i in range(0,n+1):
		for j in range(2**i,len(L),2**(i+1)):
			if (j+2**i)>len(L):
				if not j==len(L):
					L[-1]=f(L[j-1],L[-1])
				break
			L[j+2**i-1]=f(L[j-1],L[j+2**i-1])

def Downsweep(L,f,I):
	n=getintlog(len(L),2)
	L[-1]=I
	for i in range(n,0,-1):
		for j in range(0,len(L)):
			if((j+1)%(pow(2,i))==0):
				t = L[j]
				L[j]=f(t,L[j-pow(2,i-1)])
				L[j-pow(2,i-1)]=t

def fmaxp(a,i):
	return a - a%(2**i)

def Downsweep1(L,f,I):
	a=len(L)
	n=getintlog(a,2)
	if(a==1):
		L[0]=I
		return
	if(a==2**n):
		Downsweep(L,f,I)
		return 
	T=0
	L[-1]=I
	b=-1
	for i in range(n,-1,-1):
		c=fmaxp(a,i)
		if(c==a):
			T=1
		for j in range(a,0,-1):
			if j==a and (not T==1):
				if c>b:
					t=L[-1];L[-1]=f(L[-1],L[c-1]);L[c-1]=t
				b=c
			else :
				if(j%(2**(i+1))==0):
					t=L[j-1];L[j-1]=f(L[j-1],L[j-2**i-1]);L[j-2**i-1]=t

def Scan(L,f,I):
	Reduce1(L,f)
	Downsweep1(L,f,I)

def ReverseList(L):
	L1=[]
	for i in range(len(L)-1,-1,-1):
		L1.append(L[i])
	return L1



L=[1,4,1,1,1,1,1,1]
#Scan(L,plus,0)
#Reduce1(L,plus)
#print L
