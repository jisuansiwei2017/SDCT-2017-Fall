import TC_Scan as sc
from numpy import *
class pair:
	def __init__(self,i,j):
		self.a=i
		self.b=j


def op(p1,p2):
	i=fcomp(p1.a,p2.a)
	j=fplus(fmul(p1.b,p2.a),p2.b)
	res = pair(i,j)
	return res

def max(x,y): # Here we should use a binary associative function f
	if x<y:
		return y
	return x
def plus(x,y):
	return x+y
def mul(x,y):
	return x*y

def segcomp(x,y):
	if x or y :
		return 1
	else :
		return 0

def segmul(a,f):
	if(f==1):
		return 0
	else :
		return a

'''fcomp=mul
fplus=plus
fmul=mul
I=pair(1,0)
L1=[1,1,1,1,1,1]
L2=[2,2,2,2,2,2]
L=[]
for i in range(0,len(L1)):
	L.append(pair(L2[i],L1[i])) # pairs of c are stored here
sc.Scan(L,op,I)
for i in range(0,len(L)):
	print L[i].b,'''

fcomp=mul
fplus=plus
fmul=mul
m1=mat([[1,1],[1,0]]);m2=mat([0,0]);m3=mat([1,1])
La=[m1,m1,m1,m1,m1,m1,m1,m1,m1];Lb=[m3,m2,m2,m2,m2,m2,m2,m2,m2]
L=[]
#I=pair(mat([[1,0],[0,1]]),mat([0,0]))
I=pair(1,0)
for i in range(0,9):
	L.append(pair(La[i],Lb[i]))
sc.Scan(L,op,I)
for i in range(1,9):
	a=(L[i].b).tolist()
	print a[0][1]




