def Left(i):
	return (2*i)
def Right(i):
	return (2*i+1)
def max_hpf(L,i,size):
	l=Left(i);r=Right(i);n=len(L)
	if(size>n):
		return 
	if(l>size):
		return 
	i1=i-1;l1=l-1;r1=r-1
	largest = i1
	if(L[i1]<L[l1]):
		largest = l1
	if(r1<=(size-1) and L[largest]<L[r1]):
		largest = r1
	if(not(largest == i1)):
		t = L[i1];L[i1]=L[largest];L[largest]=t
		max_hpf(L,largest+1,size)

def mk_maxhp(L):
	N=(int)(len(L)/2)
	for i in range(1,N+1):
		max_hpf(L,N+1-i,len(L))

def heapsort(L,ed,st=1):
	if(ed==st):
		return 
	t=L[st-1];L[st-1]=L[ed-1];L[ed-1]=t
	max_hpf(L,st,ed-1)
	heapsort(L,ed-1,st)

def hpsort(L):
	mk_maxhp(L)
	heapsort(L,len(L))
L=[3,6,3,6,19,3,12,6,7,2,4,20]
hpsort(L)
print L