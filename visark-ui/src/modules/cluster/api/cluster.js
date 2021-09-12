import {get, postJson, del} from '@/utils/axios'

const path = {
    save: "/cluster",
    list: "/cluster/list",
    page: "/cluster/page",
    delete: "/cluster/",
}

export default {
    save(params) {
        return postJson(path.save, params);
    },
    query4Page(params) {
        return get(path.page, params);
    },
    query4List() {
        return get(path.list)
    },
    deleteById(id) {
        return del(path.delete + id)
    }
}