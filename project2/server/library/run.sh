#! /bin/sh

IN=(in0.xml in1.xml in2.xml in3.xml in4.xml in5.xml in6.xml in7.xml in8.xml in9.xml)
OUT=(out0.xml out1.xml out2.xml out3.xml out4.xml out5.xml out6.xml out7.xml out8.xml out9.xml)

for i in ${#IN[*]}
do
	java GraphServer < ${IN[$i]} | ./xdiff-xml ${OUT[$i]} -
done

