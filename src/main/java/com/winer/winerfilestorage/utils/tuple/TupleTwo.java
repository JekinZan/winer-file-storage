package com.winer.winerfilestorage.utils.tuple;

/**
 * @program: winer-file-storage-dev
 * @description: 二元组
 * @Author Jekin
 * @Date 2021/4/26
 */
public class TupleTwo<T1, T2> extends Tuple<T1> {


    private static final long serialVersionUID = 7449697621417149608L;

    private final T2 item2;

    /**
     * 实例化
     *
     */
    public TupleTwo(T1 item1, T2 item2) {
        super(item1);
        this.item2 = item2;
    }

    /**
     * 获取项目2
     *
     * @return
     */
    public T2 getItem2() {
        return item2;
    }
}
