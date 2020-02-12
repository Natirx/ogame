package data.repository;

class CaseRepositoryProvider {
    private CaseRepositoryProvider(){
    }

    private static class TemplateCaseDataRepositoryHolder {
        static final ICaseDataRepository INSTANCE_HOLDER =
                new TemplateCaseDataRepo();
    }

     static ICaseDataRepository getTemplateGoogleDataRepository(){
        return TemplateCaseDataRepositoryHolder.INSTANCE_HOLDER;
    }
}